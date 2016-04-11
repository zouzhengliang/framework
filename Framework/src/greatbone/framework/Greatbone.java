package greatbone.framework;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xnio.OptionMap;
import org.xnio.Options;
import org.xnio.Xnio;
import org.xnio.XnioWorker;

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;

/**
 * Some resources and operations shared among our modules.
 */
public class Greatbone {

    static final int CORES = Runtime.getRuntime().availableProcessors();

    public static final int IO_THREADS = CORES * 2;

    public static final int WORKER_THREADS = CORES * 16;

    public final static XnioWorker WORKER;

    static final String CONFIGXML = "config.xml";

    static Document configdoc;

    public static Element config;

    static {

        // initialize the global XNIO worker
        Xnio xnio = Xnio.getInstance(Greatbone.class.getClassLoader());
        XnioWorker worker = null;
        try {
            worker = xnio.createWorker(OptionMap.builder()
                    .set(Options.WORKER_IO_THREADS, CORES * 2)
                    .set(Options.CONNECTION_HIGH_WATER, 1000000)
                    .set(Options.CONNECTION_LOW_WATER, 1000000)
                    .set(Options.WORKER_TASK_CORE_THREADS, CORES * 8)
                    .set(Options.WORKER_TASK_MAX_THREADS, CORES * 12)
                    .set(Options.TCP_NODELAY, true)
                    .set(Options.CORK, true)
                    .getMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
        WORKER = worker;

        // load config xml
        try {
            FileInputStream file = new FileInputStream(CONFIGXML);
            DOMParser parser = new DOMParser();
            parser.parse(new InputSource(file));
            configdoc = parser.getDocument();
            config = configdoc.getDocumentElement();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // enable remote JMX through RMI
        try {
            LocateRegistry.createRegistry(8888);
            MBeanServer mserver = ManagementFactory.getPlatformMBeanServer();
            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:8888/server");
            JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mserver);
            cs.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Element getXmlTopTag(String tag) {
        NodeList lst = config.getElementsByTagName(tag);
        if (lst.getLength() > 0) {
            return (Element) lst.item(0);
        }
        return null;
    }

    public static Element childOf(Element parentel, String tag, String key) {
        Element el = null;
        NodeList lst = parentel.getElementsByTagName(tag);
        for (int i = 0; i < lst.getLength(); i++) {
            Element e = (Element) lst.item(i);
            if (key.equals(e.getAttribute("key"))) el = e;
        }
        return el;
    }

    public static String getStringFromXml() {
        DOMImplementationLS dom = (DOMImplementationLS) configdoc.getImplementation();
        LSSerializer lsSerializer = dom.createLSSerializer();
        return lsSerializer.writeToString(configdoc);
    }

}
