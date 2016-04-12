package greatbone.framework.grid;

import greatbone.framework.Greatbone;
import org.xnio.ChannelListener;
import org.xnio.OptionMap;
import org.xnio.Options;
import org.xnio.StreamConnection;
import org.xnio.channels.AcceptingChannel;

import java.io.IOException;

/**
 * The local server connectivity and RPC endpoint..
 */
class GridServer extends GridPeer {

    // the connection acceptor
    volatile AcceptingChannel<? extends StreamConnection> acceptchan;

    GridServer(GridUtility grid, String interf) {
        super(grid, interf);
    }

    public void start() throws IOException {

        // Create an accept listener.
        final ChannelListener<AcceptingChannel<StreamConnection>> acceptor = acceptchan -> {
            try {
                // channel is ready to accept zero or more connections
                for (; ; ) {
                    final StreamConnection conn = acceptchan.accept();
                    if (conn != null) {
                        // stream channel has been accepted at this stage.
                        conn.getSourceChannel().setReadListener(srcchan -> {
                            //
                            GridContext gc = new GridContext(conn);
                            handle(gc);
                        });
                    }
                }
            } catch (IOException ignored) {
            }
        };

        // server options
        OptionMap options = OptionMap.builder()
                .set(Options.RECEIVE_BUFFER, (1024 * 1024 * 4)) // large buffer for high speed LAN
                .set(Options.SEND_BUFFER, (1024 * 1024 * 4))
                .set(Options.BACKLOG, 512)
                .getMap();

        // create the server.
        acceptchan = Greatbone.WORKER.createStreamConnectionServer(address, acceptor, options);

        // start accepting connections
        acceptchan.resumeAccepts();

    }

    void handle(GridContext gc) {

    }


}
