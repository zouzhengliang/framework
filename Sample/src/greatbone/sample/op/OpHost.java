package greatbone.sample.op;

import greatbone.framework.web.*;
import greatbone.sample.Org;

import java.io.IOException;

/**
 * The operation (agents and shops) subdomain & module. [op.company.com]
 */
public class OpHost extends WebHost {

    public OpHost(WebUtility web, String key) {
        super(web, key);

        setHub(OrgControl.class, null);
    }

    @Override
    public void Get(WebContext wc) throws IOException {
        wc.sendOK((x) -> x.$("OK, it's alomost done."));
    }

    @Override
    public void Put(String rsc, WebContext exch) {

    }

    @Override
    public void Delete(String rsc, WebContext exch) {

    }


    /**
     * A hub that covers orgs.
     */
    public static class OrgControl extends WebControl implements Sub {

        public OrgControl(WebHost host, WebControl parent) {
            super(host, parent);

            // agent functions
//            addSub("shop", OrgControl.class, null);

            // shop functions
            addSub("order", OrderControl.class, null);
            addSub("user", UserControl.class, null);
            addSub("item", ItemControl.class, null);
            addSub("notice", NoticeControl.class, null);
        }

        @Override
        public WebControl locate(String key, WebContext wc) {
            Org org = null;
            if (org != null) {
                wc.setSpace(org);
                return this;
            }
            return null;
        }

    }


}
