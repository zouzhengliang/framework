package greatbone.framework.grid;

import greatbone.framework.Greatbone;
import org.xnio.ChannelListener;
import org.xnio.OptionMap;
import org.xnio.Options;
import org.xnio.StreamConnection;
import org.xnio.channels.AcceptingChannel;

import java.io.IOException;

/**
 * The local server connectivity.
 */
class GridServer extends GridPeer {

    // the connection acceptor
    volatile AcceptingChannel<? extends StreamConnection> acceptchan;

    GridServer(GridUtility grid, String ip) {
        super(grid, ip);
    }

    public void start() throws IOException {

        // Create an accept listener.
        final ChannelListener<AcceptingChannel<StreamConnection>> acceptor = acceptchan -> {
            try {
                // channel is ready to accept zero or more connections
                for (; ; ) {
                    final StreamConnection accepted = acceptchan.accept();
                    if (accepted != null) {
                        // stream channel has been accepted at this stage.
                        accepted.getSourceChannel().setReadListener(srcchan -> {
                            //To change body of implemented methods use File | Settings | File Templates.
                            GridContext context = new GridContext(accepted);
                            handle(context);
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
        acceptchan = Greatbone.WORKER.createStreamConnectionServer(soaddr, acceptor, options);

        // start accepting connections
        acceptchan.resumeAccepts();

    }

    void handle(GridContext gc) {

    }


}
