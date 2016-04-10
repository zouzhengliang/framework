package greatbone.framework.web;

import java.io.IOException;

/**
 */
public interface WebMBean {

    void start() throws IOException;

    void stop() throws IOException;

    void restart();

}
