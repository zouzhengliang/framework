package greatbone.framework.grid;

import java.io.IOException;

/**
 */
public interface GridMBean {

    void start() throws IOException;

    void stop();

    void restart() throws IOException;

    void flush();

}
