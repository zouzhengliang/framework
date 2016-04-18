package greatbone.sample.mgt;

import greatbone.framework.grid.GridUtility;
import greatbone.sample.ORGS;
import greatbone.sample.Org;
import greatbone.framework.web.WebContext;
import greatbone.framework.web.WebControl;
import greatbone.framework.web.WebHost;

/**
 */
public class AgentControl extends WebControl {

    final ORGS orgs;

    public AgentControl(WebHost host, WebControl parent) {
        super(host, parent);

        this.orgs = GridUtility.getDataSet(ORGS.class);
    }

    public void Get(WebContext wc) {
        String st = wc.qstring("status");
        if (st.equals("")) {

        }
    }

    public void Get(String rsc, WebContext wc) throws Exception {
        Org ret = orgs.getData(rsc);
        if (ret == null) {
            wc.sendNotFound();
        } else {
            // send back json
            wc.sendOK(ret);
        }
    }

    public void Post(WebContext wc) {
        Org agent = orgs.newData();

        wc.content(agent);

        orgs.put(null, agent);
    }

}
