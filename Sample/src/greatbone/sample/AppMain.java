package greatbone.sample;

import greatbone.framework.grid.GridUtility;
import greatbone.sample.mgt.MgtHost;
import greatbone.sample.op.OpHost;
import greatbone.sample.www.WwwHost;
import greatbone.framework.web.WebUtility;

import java.io.IOException;

/**
 */
public class AppMain {

    public static void main(String[] args) {

        try {
            GridUtility.initialize(
                    ITEMS.class,
                    EVENTS.class,
                    ORDERS.class,
                    ORGS.class,
                    STAFFERS.class,
                    USERS.class,
                    CLIPS.class
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // start virtual hosts
        try {
            WebUtility.createHost("mgt", MgtHost.class, null)
                    .start();
            WebUtility.createHost("op", OpHost.class, null)
                    .start();
            WebUtility.createHost("www", WwwHost.class, null)
                    .start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
