package greatbone.sample.www;

import greatbone.framework.grid.GridUtility;
import greatbone.sample.ORDERS;
import greatbone.sample.Org;
import greatbone.framework.web.WebContext;
import greatbone.framework.web.WebControl;
import greatbone.framework.web.WebHost;

/**
 * The order management handler.
 */
public class OrderControl extends WebControl {


    final ORDERS dset;

    public OrderControl(WebHost host, WebControl parent) {
        super(host, parent);

        dset = GridUtility.getDataSet(ORDERS.class);
    }

    @Override
    public void Get(WebContext wc) {
        Org org = (Org) wc.space();

        int id = 213;
        String name = "asdfadf";

    }

}
