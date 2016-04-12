package greatbone.framework.grid;

import org.xnio.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * The connectivity correponding to a particular networking peer.
 */
abstract class GridPeer {

    // the container grid instance
    final GridUtility grid;

    final String interf;

    final InetSocketAddress address;

    // the peer next to this
    GridPeer next;

    // is currently available
    boolean available;

    GridPeer(GridUtility grid, String interf) {
        this.grid = grid;
        this.interf = interf;
        this.address = new InetSocketAddress(interf, 8192);
    }

    public String interf() {
        return interf;
    }

    public void callasd() throws IOException {

        final Charset charset = Charset.forName("utf-8");
        final Xnio xnio = Xnio.getInstance();
        final XnioWorker worker = xnio.createWorker(OptionMap.EMPTY);

        try {
            final IoFuture<StreamConnection> futureConnection = worker.openStreamConnection(new InetSocketAddress("localhost", 12345), null, OptionMap.EMPTY);
            final StreamConnection channel = futureConnection.get(); // throws exceptions

            try {
                // Send the greeting
//                    Channels.writeBlocking(channel, ByteBuffer.wrap("Hello world!\n".getBytes(charset)));
//                    // Make sure all data is written
//                    Channels.flushBlocking(channel);
//                    // And send EOF
//                    channel.shutdownWrites();
                System.out.println("Sent greeting string!  The response is...");
                ByteBuffer recvBuf = ByteBuffer.allocate(128);
                // Now receive and print the whole response
//                    while (Channels.readBlocking(channel, recvBuf) != -1) {
//                        recvBuf.flip();
//                        final CharBuffer chars = charset.decode(recvBuf);
//                        System.out.print(chars);
//                        recvBuf.clear();
//                    }
            } finally {
                IoUtils.safeClose(channel);
            }
        } finally {
            worker.shutdown();
        }
    }

    public void start() throws IOException {

    }

    public void stop() {

    }

}

