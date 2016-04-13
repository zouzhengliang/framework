package greatbone.framework.grid;

import greatbone.framework.Out;
import greatbone.framework.Printer;
import greatbone.framework.web.WebContext;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * An abstract data object. that can contain one or more data entries.
 * A data object is also used internally as a cursor that points to a backing native page store.
 * A data entry has a string key
 *
 * @param <D> type of this object
 */
public abstract class GridData<D extends GridData<D>> implements Printer {

    // the associated data page
    // it is the backing store if local page and the buffer field is null
    GridPage<D> page;

    // the byte array that contains contents of data entries
    byte[] content;
    int length; // actual number of entries

    // current index
    int index;

    // hash map buckets, can be null
    int[] buckets;

    void init(int capacity) {
        this.content = new byte[schema().size * capacity];
    }

    void init(GridLocalPage<D> page) {
        this.page = page;
    }

    public int next(boolean grow) {
        return -1;
    }

    public void parse(WebContext wc) {
        // parse data according to schema

    }

    public void save(WebContext x) {
    }

    // for direct sending thru a stream channel
    ByteBuffer wrap() {
        return ByteBuffer.wrap(content, 0, length * schema().size);
    }

    short getShort(int off) {
        if (content != null) {
            int p = schema().size * index + off;
            return (short) ((content[p++] << 8) + content[p]);
        } else {
            return ((GridLocalPage<D>) page).getShort(index, off);
        }
    }

    void putShort(int off, short v) {
        if (content != null) {
            int p = schema().size * index + off;
            content[p++] = (byte) ((v >>> 8) & 0xff);
            content[p] = (byte) (v & 0xff);
        }
    }

    int getInt(int off) {
        if (content != null) {
            int p = schema().size * index + off;
            return content[p++] + (content[p++] << 8) + (content[p++] << 16) + (content[p] << 24);
        } else {
            return ((GridLocalPage<D>) page).eint(index, off);
        }
    }

    void putInt(int off, int v) {
        if (content != null) {
            int p = schema().size * index + off;
            content[p++] = (byte) (v & 0xff);
            content[p++] = (byte) ((v >>> 8) & 0xff);
            content[p++] = (byte) ((v >>> 16) & 0xff);
            content[p] = (byte) ((v >>> 24) & 0xff);
        }
    }

    int compareString(int off, int len) {
        return -1;
    }

    String getString(int off, int len) {
        int siz = schema().size;
        if (page != null) {
            return ((GridLocalPage<D>) page).estring(index, off);
        } else {
            StringBuilder sb = null;
            int p = siz * index + off;
            while (p < len) {
                char c = (char) ((content[p++] << 8) + content[p++]);
                if (c == 0) {
                    break;
                } else { // got a valid character
                    if (sb == null) sb = new StringBuilder(siz);
                    sb.append(c);
                }
            }
            return (sb == null) ? null : sb.toString();
        }
    }

    void putString(int off, String v, int len) {
        int p = schema().size * index + off;
        int min = Math.min(len, v.length());
        for (int i = 0; i < min; i++) {
            char c = v.charAt(i);
            content[p++] = (byte) ((c >>> 8) & 0xff);
            content[p++] = (byte) (c & 0xff);
        }
    }

    int compareAscii(int off, int len) {
        return -1;
    }

    String getAscii(int off, int len) {
        int siz = schema().size;
        if (content != null) {
            StringBuilder sb = null;
            int p = siz * index + off;
            while (p < len) {
                char c = (char) content[p++];
                if (c == 0) {
                    break;
                } else { // got a valid character
                    if (sb == null) sb = new StringBuilder(siz);
                    sb.append(c);
                }
            }
            return (sb == null) ? null : sb.toString();
        } else {
            return ((GridLocalPage<D>) page).estring(index, off);
        }
    }

    void putAscii(int off, String v, int len) {
        int p = schema().size * index + off;
        int min = Math.min(len, v.length());
        for (int i = 0; i < min; i++) {
            char c = v.charAt(i);
            content[p++] = (byte) (c & 0xff);
        }
    }

    //
    // CURSOR OPERATION
    //

    public void previous() {
        index--;
    }

    public void move() {
        index++;
    }

    @Override
    public void print(Out out) throws IOException {

    }

    protected abstract GridSchema<D> schema();

}
