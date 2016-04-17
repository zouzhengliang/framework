package greatbone.framework.web;

import greatbone.framework.Config;
import greatbone.framework.Greatbone;
import greatbone.framework.util.Roll;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * The managerial point of web services.
 */
public class WebUtility implements WebMBean, Config {

    // singleton instance
    static WebUtility INST;

    final Element config;

    final Roll<String, WebStatic> statics;

    // virtual hosts maintained by this JVM process
    final ArrayList<WebHost> hosts = new ArrayList<>(4);

    WebUtility() {
        this.config = Greatbone.getXmlTopTag("web");

        // load static resources
        statics = new Roll<>(256);
        File dir = new File("./RES/");
        if (dir.exists() && dir.isDirectory()) {
            //noinspection ConstantConditions
            for (File file : dir.listFiles()) {
                int flen = (int) file.length(); // NOTE: large file not supported
                byte[] content = new byte[flen];
                try {
                    FileInputStream in = new FileInputStream(file);
                    if (flen != in.read(content)) { // never happen
                        throw new IOException("file reading error: " + file.getName());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                WebStatic sta = new WebStatic(file.getName(), content);
                statics.put(sta.key, sta);
            }
        }
    }

    WebStatic getStatic(String key) {
        return statics.get(key);
    }

    <T extends WebHost> T addHost(String key, Class<T> clazz, Checker checker) {
        try {
            Constructor<T> ctor = clazz.getConstructor(WebUtility.class, String.class);
            T host = ctor.newInstance(this, key);
            host.checker = checker;
            hosts.add(host);
            return host;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void start() throws IOException {
        for (WebHost host : hosts) {
            host.start();
        }
    }

    @Override
    public void stop() throws IOException {
        for (WebHost host : hosts) {
            host.stop();
        }
    }

    @Override
    public void restart() {
        for (WebHost host : hosts) {
        }
    }


    @Override
    public Element config() {
        return config;
    }

    public static <T extends WebHost> T createHost(String key, Class<T> clazz, Checker checker) {
        if (INST == null) {
            INST = new WebUtility();
        }
        return INST.addHost(key, clazz, checker);
    }

    public static final String
            NONE = "application/octet-stream",
            CER = "application/x-x509-ca-cert",
            CLASS = "application/java-vm",
            CSS = "text/css",
            DOC = "application/msword",
            GIF = "image/gif",
            HTM = "text/html",
            HTML = "text/html",
            JPEG = "image/jpeg",
            JPG = "image/jpeg",
            JS = "application/x-javascript",
            MOV = "video/quicktime",
            MP3 = "audio/mpeg",
            MPEG = "video/mpeg",
            MPG = "video/mpeg",
            PDF = "application/pdf",
            PNG = "image/png",
            PPT = "application/vnd.ms-powerpoint",
            QT = "video/quicktime",
            RTF = "application/rtf",
            SVG = "image/svg+xml",
            SWF = "application/x-shockwave-flash",
            TAR = "application/x-tar",
            TXT = "text/plain",
            XLS = "application/vnd.ms-excel",
            XML = "application/xml",
            ZIP = "application/zip";

    static final Roll<String, String> types = new Roll<>(256);

    static {
        types.put("", NONE);
        types.put("cer", CER);
        types.put("class", CLASS);
        types.put("css", CSS);
        types.put("doc", DOC);
        types.put("gif", GIF);
        types.put("htm", HTML);
        types.put("html", HTML);
        types.put("jpeg", JPEG);
        types.put("jpg", JPEG);
        types.put("js", JS);
        types.put("mov", MOV);
        types.put("mp3", MP3);
        types.put("mpeg", MPEG);
        types.put("mpg", MPEG);
        types.put("pdf", PDF);
        types.put("png", PNG);
        types.put("ppt", PPT);
        types.put("qt", QT);
        types.put("rtf", RTF);
        types.put("svg", SVG);
        types.put("swf", SWF);
        types.put("tar", TAR);
        types.put("txt", TXT);
        types.put("xls", XLS);
        types.put("xml", XML);
        types.put("zip", ZIP);
    }

    public static String getMimeType(String extension) {
        String mime = types.get(extension);
        return mime != null ? mime : NONE;
    }

    //
    // CLOCK
    //

    static final int SEC = 1000; // aligned to second

    static volatile long NOW = System.currentTimeMillis() / SEC * SEC;

    static final Thread CLOCK = new Thread("Clock") {
        public void run() {
            while (!interrupted()) {
                try {
                    Thread.sleep(1000);
                    NOW = System.currentTimeMillis() / SEC * SEC;
                } catch (InterruptedException e) {
                }
            }
        }
    };

    static {
        CLOCK.start();
    }

    public static long now() {
        return NOW;
    }

}