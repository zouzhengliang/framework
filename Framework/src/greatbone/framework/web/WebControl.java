package greatbone.framework.web;

import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import greatbone.framework.util.Roll;

import java.lang.reflect.Constructor;

/**
 * A set of actions working on request/response eachanges to carry out management tasks on a collection of resources.
 */
public abstract class WebControl {

    // the root handler
    protected final WebHost host;

    // the parent of this work instance, if any
    protected final WebControl parent;

    // name as appeared in the uri
    String key;

    // access checker
    Checker checker;

    // the subordinate structures
    Sub subordinate;

    // execution of background tasks
    Thread cycler;

    protected WebControl(WebHost host, WebControl parent) {
        this.host = (host != null) ? host : (WebHost) this;
        this.parent = parent;

        // initialize the cycler thread if any
        if (this instanceof Runnable) {
            cycler = new Thread((Runnable) this);
            cycler.start();
        }
    }

    public <T extends WebControl> void addSub(String key, Class<T> controller, Checker checker) {
        try {
            Constructor<T> ctor = controller.getConstructor(WebHost.class, WebControl.class);
            T sub = ctor.newInstance(host, this);
            sub.key = key;
            sub.checker = checker;
            if (this.subordinate == null) {
                this.subordinate = new Children(8);
            }
            ((Children) this.subordinate).add(sub);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String key() {
        return key;
    }

    public Sub children() {
        return subordinate;
    }

    /**
     * To handle a request/response exchange by this or by an appropriate sub controller.
     *
     * @param base the relative URI that this controller is based
     * @param exch the request.response exchange
     */
    protected void perform(String base, WebContext exch) throws Exception {
        int slash = base.indexOf('/');
        if (slash == -1) { // without a slash then handle by this controller instance
            exch.control = this;
            HttpString method = exch.method();
            if (method == Methods.GET) Get(exch);
            else if (method == Methods.POST) Post(exch);
            else if (method == Methods.PUT) Put(null, exch);
            else if (method == Methods.DELETE) Delete(null, exch);
        } else if (subordinate != null) { // resolve the sub structure
            WebControl controller = subordinate.locate(base.substring(0, slash), exch);
            if (controller != null) {
                controller.perform(base.substring(slash + 1), exch);
            } else {
                exch.sendNotFound();
            }
        } else {
            exch.sendNotFound();
        }
    }

    public void Get(WebContext exch) throws Exception {
    }

    public void Get(String rsc, WebContext exch) throws Exception {
    }

    /**
     * The POST verb is most-often utilized to **create** new resources. In particular, it's used to create subordinate resources. That is, subordinate to some other (e.g. parent) resource. In other words, when creating a new resource, POST to the parent and the service takes care of associating the new resource with the parent, assigning an ID (new resource URI), etc.
     * <p/>
     * On successful creation, return HTTP status 201, returning a Location header with a link to the newly-created resource with the 201 HTTP status.
     *
     * @param exch a request/response exchange
     * @throws Exception
     */
    public void Post(WebContext exch) throws Exception {
    }

    public void Put(String rsc, WebContext exch) throws Exception {
    }

    public void Delete(String rsc, WebContext exch) throws Exception {
    }

    public void Patch(String rsc, WebContext exch) throws Exception {
    }

    /**
     * A hashmap-based implemention that holds a set of sub works.
     */
    static class Children implements Sub {

        final Roll<String, WebControl> controllers;

        Children(int initial) {
            this.controllers = new Roll<>(initial);
        }

        public WebControl locate(String key, WebContext exch) {
            return controllers.get(key);
        }

        void add(WebControl v) {
            controllers.put(v.key, v);
        }

    }

}
