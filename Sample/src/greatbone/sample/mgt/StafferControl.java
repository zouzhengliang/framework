package greatbone.sample.mgt;

import greatbone.framework.grid.GridUtility;
import greatbone.sample.STAFFERS;
import greatbone.sample.Staffer;
import greatbone.framework.web.Pairs;
import greatbone.framework.web.WebContext;
import greatbone.framework.web.WebControl;
import greatbone.framework.web.WebHost;

import java.io.IOException;

/**
 * The management of staffers.
 */
public class StafferControl extends WebControl {

    final STAFFERS staffers;

    public StafferControl(WebHost host, WebControl parent) {
        super(host, parent);

        this.staffers = GridUtility.getDataSet(STAFFERS.class);
    }

    public void Get(WebContext wc) throws IOException {

        // get all records
//        Staffer[] lst = staffers.search(null,null);

        // send the list in json format
//        wc.sendOK(lst);
    }

    @Override
    public void Get(String rsc, WebContext wc) throws Exception {

        Pairs cnt = wc.content();
        String key = cnt.getFirst("key");

        // get all records
        Staffer obj = staffers.getData(key);

        wc.sendOK(obj);
    }

    public void Post(WebContext wc) {

        Staffer new_ = staffers.newData();

        wc.content(new_); // validate the input

        staffers.put(null, new_);

    }

}
