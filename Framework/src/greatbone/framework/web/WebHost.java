package greatbone.framework.web;


import io.undertow.UndertowOptions;
import io.undertow.connector.ByteBufferPool;
import io.undertow.server.DefaultByteBufferPool;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.protocol.http.HttpOpenListener;
import io.undertow.util.HttpString;
import greatbone.framework.Config;
import greatbone.framework.Greatbone;
import org.w3c.dom.Element;
import org.xnio.*;
import org.xnio.channels.AcceptingChannel;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Base64;

import static io.undertow.util.Headers.IF_MODIFIED_SINCE;
import static io.undertow.util.Methods.GET;
import static io.undertow.util.Methods.HEAD;
import static io.undertow.util.StatusCodes.*;

/**
 * A root web folder that may have a hub handler which deals with variable sector folders.
 */
public abstract class WebHost extends WebControl implements HttpHandler, WebHostMBean, Config {

    static final String EMPTY = "";

    static final Base64.Decoder DEC = Base64.getMimeDecoder();

    static final Base64.Encoder ENC = Base64.getMimeEncoder();

    final WebUtility parent;

    final Element config;

    final String hostname;

    final int port;

    final InetSocketAddress address;

    volatile AcceptingChannel<? extends StreamConnection> server;

    boolean ssl;

    protected WebHost(WebUtility parent, String key) {
        super(null, null);
        this.parent = parent;
        this.key = key;

        // register as mbean
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName objname = new ObjectName("web:type=Host");
            mbs.registerMBean(this, objname);
        } catch (Exception e) {
        }

        // get address settings from configuration, can be null if no configuration for the host is found
        this.config = Greatbone.childOf(parent.configel, "host", key);
        this.hostname = (config != null) ? config.getAttribute("hostname") : null;
        this.port = (config != null) ? Integer.parseInt(config.getAttribute("port")) : 80;
        this.address = (hostname == null) ? null : new InetSocketAddress(hostname, port);

    }

    public <T extends WebControl & Sub> void setHub(Class<T> clazz, Checker checker) {
        try {
            Constructor<T> ctor = clazz.getConstructor(WebHost.class, WebControl.class);
            T sub = ctor.newInstance(this, this);
            sub.checker = checker;
            this.subordinate = sub;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String hostname() {
        return hostname;
    }

    public int port() {
        return port;
    }

    /**
     * Starts the service with the specified executor. A same executor can be shared among multiple services.
     */
    public void start() throws IOException {
        if (address == null) {
            return;
        }

        OptionMap options = OptionMap.builder()
                .set(Options.TCP_NODELAY, true)
                .set(Options.REUSE_ADDRESSES, true)
                .set(Options.BALANCING_TOKENS, 1)
                .set(Options.BALANCING_CONNECTIONS, 2)
                .set(Options.BACKLOG, 1024)
                .getMap();

        OptionMap serverOptions = OptionMap.builder()
                .set(UndertowOptions.NO_REQUEST_TIMEOUT, 60000000)
                .getMap();


        // make undertow outputstream efficient by applying relatively large buffers
        ByteBufferPool buffers = new DefaultByteBufferPool(true, 1024 * 32, -1, 4);

        OptionMap undertowOptions = OptionMap.builder().set(UndertowOptions.BUFFER_PIPELINED_DATA, true).addAll(serverOptions).getMap();
        HttpOpenListener openListener = new HttpOpenListener(buffers, undertowOptions);
        openListener.setRootHandler(this);
        ChannelListener<AcceptingChannel<StreamConnection>> acceptListener = ChannelListeners.openListenerAdapter(openListener);
        server = Greatbone.WORKER.createStreamConnectionServer(address, acceptListener, options);
        server.resumeAccepts();
    }

    public void stop() throws IOException {
        if (address == null) {
            return;
        }
        if (server.isOpen()) {
            server.close();
        }
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {

        // handle static resources in a IO thread
        String path = exchange.getRelativePath();
        String base = path.substring(1);
        int dot = base.lastIndexOf('.');
        if (dot != -1) {
            WebStatic sta = parent.getStatic(base);
            handleStatic(sta, exchange);
            exchange.endExchange();
            return;
        }

        // displatch to a task thread for dynamic content handling
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
        }

        // NOTE: by design, the exchange is closed after this processing
        try (WebContext wc = new WebContext(this, exchange)) {
            // BASIC authentication from client
            authenticate(wc);

            perform(base, wc);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("deprecation")
    void authenticate(WebContext wc) {
        String v = wc.authorization();
        if (v == null) {
            return;
        }
        if (!v.startsWith("Basic ")) {

        }

        int len = v.length() - 6;
        byte[] ba = new byte[len];
        for (int i = 0; i < len; i++) { // a quick copy of bytes
            ba[i] = (byte) v.charAt(6 + i);
        }
        byte[] ret = DEC.decode(ba);

        int p = 0;
        while (ret[p++] != (byte) ':') ;
        String username = new String(ret, 0, 0, p); // we use this direct string construction for speed
        String password = new String(ret, 0, p + 1, len - p);
        Principal prin = acquire(username, password);

        wc.principal = prin;
    }

    protected Principal acquire(String username, String password) {
        return null;
    }

    void handleStatic(WebStatic sta, HttpServerExchange exch) {
        if (sta == null) {
            exch.setStatusCode(NOT_FOUND);
        } else {
            HttpString method = exch.getRequestMethod();
            if (method == GET) {
                String since = exch.getRequestHeaders().getFirst(IF_MODIFIED_SINCE);
                if (since != null) {
                    exch.setStatusCode(NOT_MODIFIED);
                } else {
                    // async sending
                    exch.getResponseSender().send(ByteBuffer.wrap(sta.content));
                }
            } else if (method == HEAD) {
            } else {
                exch.setStatusCode(METHOD_NOT_ALLOWED);
            }
        }
    }

    @Override
    public Element config() {
        return config;
    }


}
