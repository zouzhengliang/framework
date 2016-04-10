package greatbone.framework;

import java.io.IOException;

/**
 */
public interface Out<U extends Out<U>> {

    U $(boolean v) throws IOException;

    U $(short v) throws IOException;

    U $(int v) throws IOException;

    U $(long v) throws IOException;

    U $(CharSequence v) throws IOException;

}
