package greatbone.sample.op;

import greatbone.sample.Org;
import greatbone.framework.web.WebControl;
import greatbone.framework.web.WebContext;
import greatbone.framework.web.WebHost;

/**
 * The order management handler.
 */
public class UserControl extends WebControl {


    public UserControl(WebHost service, WebControl parent) {
        super(service, parent);

    }

    @Override
    public void Get(WebContext exch) {
        Org org = (Org)exch.space()  ;


    }
}
