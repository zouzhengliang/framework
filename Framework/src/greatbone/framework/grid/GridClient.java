package greatbone.framework.grid;

import greatbone.framework.Greatbone;
import org.xnio.OptionMap;
import org.xnio.Options;
import org.xnio.StreamConnection;

import java.io.IOException;
import java.util.ArrayDeque;

/**
 * The client connectivity and RPC endpoint to a particular member node.
 */
class GridClient extends GridEndPoint {

    static final OptionMap OPTIONS = OptionMap.builder()
            .set(Options.RECEIVE_BUFFER, (1024 * 1024 * 4)) // large buffer for high speed LAN
            .set(Options.SEND_BUFFER, (1024 * 1024 * 4))
            .set(Options.BACKLOG, 256)
            .set(Options.ALLOW_BLOCKING, true) // enable blocking I/O
            .set(Options.KEEP_ALIVE, true)
            .getMap();

    // failed to connect or communicate
    boolean trouble;

    // time last tried
    volatile long tried;

    // the connection pool
    final int poolsiz;
    final ArrayDeque<StreamConnection> pool;

    GridClient(GridUtility grid, String interf, int poolsiz) throws IOException {
        super(grid, interf);

        this.poolsiz = poolsiz;
        this.pool = new ArrayDeque<>(poolsiz);
    }

    final StreamConnection checkout() throws IOException {
        StreamConnection conn;
        synchronized (pool) {
            conn = pool.poll();
        }
        if (conn == null) {
            conn = Greatbone.WORKER.openStreamConnection(address, null, OPTIONS).get();
        }
        return conn;
    }

    final void checkin(StreamConnection conn) {
        if (conn != null) {
            synchronized (pool) {
                if (pool.size() == poolsiz) { // if pool already full then drop the conn
                    try {
                        conn.close();
                    } catch (IOException e) {
                    }
                } else {
                    pool.push(conn);
                }
            }
        }
    }

    // browse the peer of dataset paging information
    Object browse() {
        StreamConnection conn = null;
        try {
            conn = checkout();
            GridContext gc = new GridContext(conn);
            // set header

            gc.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            checkin(conn);
        }
        return null;
    }

    // send call for a get operation to the remote
    void call(GridContext gc) {
        StreamConnection conn = null;
        try {
            conn = checkout();
            // send and receive

            gc.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            checkin(conn);
        }
    }

}

