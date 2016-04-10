package greatbone.framework.grid;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

/**
 * evictable file  cache
 */
class GridFolder {

    String name;

    final ConcurrentHashMap<String, GridFile> cache;

    GridFolder(int capacity) {
        cache = new ConcurrentHashMap<>(capacity);
    }

    public GridFile get(String key) throws IOException {
        GridFile info = cache.get(key);
        if (info == null) {
            // It's OK to construct a Heavy that ends up not being used
            info = new GridFile(Paths.get(key));
            GridFile other = cache.putIfAbsent(key, info);
            if (other != null) {
                info = other;
            }
        }
        return info;
    }

}
