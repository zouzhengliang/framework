package greatbone.sample.www;

import greatbone.framework.web.*;
import greatbone.sample.HTML;
import greatbone.sample.Org;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * The public web interface for ordinary user access. [www.company.com]
 */
public class WwwHost extends WebHost {

    public WwwHost(WebUtility parent, String key) {
        super(parent, key);

        setHub(ShopControl.class, null);
    }

    @Override
    protected Principal acquire(String username, String password) {
        return null;
    }

    @Override
    public void Get(WebContext exch) throws IOException {
        exch.$("Hello, world!");
        exch.sendOK(new HTML() {
        });
    }

    @Override
    public void Put(String rsc, WebContext exch) {

    }

    @Override
    public void Delete(String rsc, WebContext exch) {

    }

    //
    // MAIN
    //

    public static final int CORES = Runtime.getRuntime().availableProcessors();

    // thread pool executor for all services in the JVM
    public static final Executor EXECUTOR = Executors.newFixedThreadPool(CORES * 16);


    static {
        Properties props = new Properties();
        props.setProperty("vhosts", "");

//        props.load(new FileReader("operation.properties"));

        props.getProperty("vhosts", "");
    }


    /**
     * A hub that covers all shops.
     */
    public static class ShopControl extends WebControl implements Sub {

        public ShopControl(WebHost root, WebControl parent) {
            super(root, parent);

            addSub("order", OrderControl.class, null);
        }

        @Override
        public WebControl locate(String key, WebContext exch) {
            Org org = null;
            if (org != null) {
                exch.setSpace(org);
                return this;
            }
            return null;
        }

    }


}
