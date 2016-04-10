package greatbone.sample;

import greatbone.framework.Out;
import greatbone.framework.web.WebView;

import java.io.IOException;

/**
 */
public class JSON extends WebView<JSON> {

    @Override
    protected String ctype() {
        return "application/json";
    }

    @Override
    public void print(Out out) throws IOException {

    }

}
