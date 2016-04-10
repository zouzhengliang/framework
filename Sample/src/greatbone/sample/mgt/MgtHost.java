package greatbone.sample.mgt;

import greatbone.framework.web.WebContext;
import greatbone.framework.web.WebHost;
import greatbone.framework.web.WebUtility;

import java.io.IOException;

/**
 * The management subdomain & module. [mgt.company.com]
 */
public class MgtHost extends WebHost {

    public MgtHost(WebUtility parent, String key) {
        super(parent, key);

        addSub("staffer", StafferControl.class, null);
        addSub("agent", AgentControl.class, null);
        addSub("user", UserControl.class, null);
        addSub("event", EventControl.class, null);
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

}
