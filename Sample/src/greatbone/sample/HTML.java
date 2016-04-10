package greatbone.sample;

import greatbone.framework.Out;
import greatbone.framework.web.WebView;

import java.io.IOException;

/**
 */
public abstract class HTML extends WebView<HTML> {

    @Override
    protected String ctype() {
        return "text/html; charset=UTF-8";
    }

    @Override
    public void print(Out out) throws IOException {
        $("<!DOCTYPE HTML>");
        $("<html>");
        $("<head>");
        $("<link rel=\"stylesheet\" type=\"text/css\" href=\"/foundation.min.css\" media=\"screen\" />");
        $("<script type=\"text/javascript\"  src=\"jquery.min.js\"></script>");
        $("<script type=\"text/javascript\"  src=\"foundation.min.js\"></script>");
        $("</head>");

        body();

        $("<body>");
        $("</body>");
        $("</html>");
    }

    protected void body() throws IOException {

        $("<a class=\"button\" href=\"#\">An Active Button</a>\n" +
                "<button class=\"button button-inactive\">An Active Button</button>");

        $("<div class=\"button-group\">\n" +
                "  <a class=\"secondary button\">View</a>\n" +
                "  <a class=\"success button\">Edit</a>\n" +
                "  <a class=\"warning button\">Share</a>\n" +
                "  <a class=\"alert button\">Delete</a>\n" +
                "</div>");

    }

    public void $esc(char c) {

    }

    public void $esc(CharSequence cs) {

    }

}
