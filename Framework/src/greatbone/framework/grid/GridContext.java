package greatbone.framework.grid;

import io.undertow.server.DefaultByteBufferPool;
import org.xnio.StreamConnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * A grid call & reply exchange.
 */
class GridContext {

    static final DefaultByteBufferPool RPOOL = new DefaultByteBufferPool(true, 64, -1, 4);

    static final DefaultByteBufferPool CPOOL = new DefaultByteBufferPool(true, 1024 * 64, -1, 4);

    StreamConnection connection;

    ByteBuffer callhead;

    ByteBuffer cbody;

    InputStream istream = new InputStream() {
        @Override
        public int read() throws IOException {
            return 0;
        }
    };

    ByteBuffer replyhead;


    ByteBuffer rbody;

    OutputStream ostream = new OutputStream() {
        @Override
        public void write(int b) throws IOException {

        }
    };


    GridContext(StreamConnection connection) {
        this.connection = connection;
        this.callhead = CPOOL.allocate().getBuffer();
        this.replyhead = RPOOL.allocate().getBuffer();
    }

    void send(StreamConnection conn) throws IOException {
        conn.getSinkChannel().write(cbody);
    }

    void send(StreamConnection conn, ByteBuffer add) throws IOException {
        conn.getSinkChannel().write(new ByteBuffer[]{cbody, add});
    }

    void receive(StreamConnection conn) throws IOException {
        conn.getSourceChannel().read(cbody);
    }

    void receive(StreamConnection conn, ByteBuffer add) throws IOException {
        conn.getSourceChannel().read(new ByteBuffer[]{cbody, add});
    }

}

