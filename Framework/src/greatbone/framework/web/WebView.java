package greatbone.framework.web;

import greatbone.framework.Out;
import greatbone.framework.Printer;

import java.io.IOException;

/**
 */
@SuppressWarnings("unchecked")
public abstract class WebView<U extends Out<U>> implements Out<U>, Printer {

    // the wrapped output object
    WebContext wc;

    protected abstract String ctype();

    @Override
    public abstract void print(Out out) throws IOException;

    @Override
    public U $(boolean v) throws IOException {
        wc.$(v);
        return (U) this;
    }

    @Override
    public U $(short v) throws IOException {
        wc.$(v);
        return (U) this;
    }

    @Override
    public U $(int v) throws IOException {
        wc.$(v);
        return (U) this;
    }

    @Override
    public U $(long v) throws IOException {
        wc.$(v);
        return (U) this;
    }

    @Override
    public U $(CharSequence v) throws IOException {
        wc.$(v);
        return (U) this;
    }

}
