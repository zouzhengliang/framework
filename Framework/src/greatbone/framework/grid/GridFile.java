package greatbone.framework.grid;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * A memory copy of file
 */
class GridFile {

    String name;

    // direct buffer that holds the content bytes
    ByteBuffer buffer;

    long modified;

    // access counter
    volatile int counter;

    GridFile(Path file) throws IOException {
        BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
        int size = (int) attr.size();
        buffer = ByteBuffer.allocateDirect(size);

        try (SeekableByteChannel channel = Files.newByteChannel(file)) {
            channel.read(buffer);
        } catch (IOException e) {

        }
    }


}
