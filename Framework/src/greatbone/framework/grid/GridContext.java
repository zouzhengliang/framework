package greatbone.framework.grid;

import io.undertow.server.DefaultByteBufferPool;
import org.xnio.StreamConnection;
import org.xnio.conduits.ConduitStreamSinkChannel;
import org.xnio.conduits.ConduitStreamSourceChannel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * A call & reply exchange between two grid endpoints.
 */
class GridContext {

    // buffer pool for calls, replies and streams
    static final DefaultByteBufferPool
            CALLP = new DefaultByteBufferPool(true, 1024 * 64, -1, 4),
            REPLYP = new DefaultByteBufferPool(true, 64, -1, 4),
            STREAMP = new DefaultByteBufferPool(true, 64, -1, 4);

    final StreamConnection conn;

    // the header part of the call
    final ByteBuffer call;

    // input stream for call headers
    InputStream istream;

    // the header part of the reply
    final ByteBuffer reply;

    // output stream for reply body
    OutputStream ostream;


    GridContext(StreamConnection conn) {
        this.conn = conn;
        this.call = CALLP.allocate().getBuffer();
        this.reply = REPLYP.allocate().getBuffer();
    }

    final InputStream getInputStream() {
        if (istream == null) {
            istream = new InputStream() {
                final ConduitStreamSourceChannel chan = conn.getSourceChannel();
                ByteBuffer buf;

                @Override
                public int read() throws IOException {
                    return 0;
                }
            };
        }
        return istream;
    }

    final OutputStream getOutputStream() {
        if (ostream == null) {
            final OutputStream ostream = new OutputStream() {
                final ConduitStreamSinkChannel chan = conn.getSinkChannel();
                ByteBuffer buf;

                @Override
                public void write(int b) throws IOException {
                    chan.write(buf);
                }
            };
        }
        return ostream;
    }

    void close() throws IOException {
        if (istream != null) {
            istream.close();
        }
        if (ostream != null) {
            ostream.close();
        }
    }

}

