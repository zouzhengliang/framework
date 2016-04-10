package greatbone.framework.grid;

import greatbone.framework.MyFarm;
import greatbone.framework.util.Pool;
import org.xnio.OptionMap;
import org.xnio.Options;
import org.xnio.StreamConnection;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * The client connectivity through RPC.
 */
class GridClient extends GridPeer {

    static final OptionMap OPTIONS = OptionMap.builder()
            .set(Options.RECEIVE_BUFFER, (1024 * 1024)) // large buffer for high speed LAN
            .set(Options.SEND_BUFFER, (1024 * 1024))
            .set(Options.BACKLOG, 256)
            .set(Options.ALLOW_BLOCKING, true) // use blocking I/O
            .set(Options.KEEP_ALIVE, true)
            .getMap();

    // connection pool
    final Pool<StreamConnection> pool;


    GridClient(GridUtility grid, String ip, int pooled) throws IOException {
        super(grid, ip);

        pool = new Pool<StreamConnection>(pooled) {
            protected StreamConnection create() throws IOException {
                return MyFarm.WORKER.openStreamConnection(soaddr, null, OPTIONS).get();
            }
        };
    }

    StreamConnection connection() throws Exception {
        return pool.checkout();
    }

    void call(GridContext gc) throws IOException {
        StreamConnection conn = null;
        try {
            conn = pool.checkout();
            gc.send(pool.checkout());
            // send message to call remote peer
            pool.checkout().getSinkChannel();

        } finally {
            pool.checkin(conn); // no wait
        }
    }

    <K, D extends GridData<D>> D search(K dskey, K pageid, Critera<D> filter) {
        StreamConnection conn = null;
        try {
            conn = pool.checkout();
            GridContext gc = new GridContext(conn);
            ObjectOutputStream out = new ObjectOutputStream(gc.ostream);
            out.writeObject(filter);

            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pool.checkin(conn); // no wait
        }
        return null;
    }

}

