package greatbone.sample.mgt;

import greatbone.framework.grid.GridUtility;
import greatbone.sample.EVENTS;
import greatbone.sample.Event;
import greatbone.framework.web.WebContext;
import greatbone.framework.web.WebControl;
import greatbone.framework.web.WebHost;

/**
 * The menu management handler.
 */
public class EventControl extends WebControl {

    final EVENTS events;

    public EventControl(WebHost host, WebControl parent) {
        super(host, parent);

         events= GridUtility.getDataSet(EVENTS.class);

    }

    @Override
    public void Get(WebContext exch) {


    }

    @Override
    public void Get(String rsc, WebContext exch) throws Exception {

        int key = Integer.parseInt(rsc);
//        events.partition(key);
    }

    public void Post(WebContext exch) {

        // create and insert

        Event _new = new Event();
        if (true) {
//            _new.load(exch);
        }  else {
            // explicitly set fields
            _new.id(12);
            _new.text("asfsdfsadf");

        }

        events.put(null,_new) ; // set autogen id during the put

    }

}
