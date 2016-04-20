package greatbone.framework.grid;

import greatbone.framework.Out;
import greatbone.framework.Printer;
import greatbone.framework.web.WebContext;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * An abstract data object. that can contain one or more data records.
 * A data object is also used internally as a cursor that points to a backing native page store.
 * A data entry has a string key
 *
 * @param <D> type of this object
 */
public abstract class GridData<D extends GridData<D>> implements Printer {

    // the associated data page
    // it is the backing store if native page and the buffer field is null
    GridPage<D> page;

    // contents of data records
    byte[] buffer;
    int size; // actual number of records

    // current index
    int index;

    // hash map buckets, can be null
    int[] buckets;

    void init(int capacity) {
        this.buffer = new byte[schema().size * capacity];
    }

    void init(GridPageX<D> page) {
        this.page = page;
    }

    public int next(boolean grow) {
        return -1;
    }

    public void add(D dat) {

    }

    public void parse(WebContext wc) {
        // parse data according to schema

    }

    public void save(WebContext x) {
    }

    // for direct sending thru a stream channel
    ByteBuffer wrap() {
        return ByteBuffer.wrap(buffer, 0, size * schema().size);
    }

    short getShort(int off) {
        if (buffer != null) {
            int p = schema().size * index + off;
            return (short) ((buffer[p++] << 8) + buffer[p]);
        } else {
            return ((GridPageX<D>) page).getShort(index, off);
        }
    }

    void putShort(int off, short v) {
        if (buffer != null) {
            int p = schema().size * index + off;
            buffer[p++] = (byte) ((v >>> 8) & 0xff);
            buffer[p] = (byte) (v & 0xff);
        } else {

        }
    }

    int getInt(int off) {
        if (buffer != null) {
            int p = schema().size * index + off;
            return buffer[p++] + (buffer[p++] << 8) + (buffer[p++] << 16) + (buffer[p] << 24);
        } else {
            return ((GridPageX<D>) page).getInt(index, off);
        }
    }

    void putInt(int off, int v) {
        if (buffer != null) {
            int p = schema().size * index + off;
            buffer[p++] = (byte) (v & 0xff);
            buffer[p++] = (byte) ((v >>> 8) & 0xff);
            buffer[p++] = (byte) ((v >>> 16) & 0xff);
            buffer[p] = (byte) ((v >>> 24) & 0xff);
        }
    }

    int compareString(int off, int len) {
        return -1;
    }

    String getString(int off, int len) {
        int siz = schema().size;
        if (page != null) {
            return ((GridPageX<D>) page).getString(index, off);
        } else {
            StringBuilder sb = null;
            int p = siz * index + off;
            while (p < len) {
                char c = (char) ((buffer[p++] << 8) + buffer[p++]);
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
            buffer[p++] = (byte) ((c >>> 8) & 0xff);
            buffer[p++] = (byte) (c & 0xff);
        }
    }

    int compareAscii(int off, int len) {
        return -1;
    }

    String getAscii(int off, int len) {
        int siz = schema().size;
        if (buffer != null) {
            StringBuilder sb = null;
            int p = siz * index + off;
            while (p < len) {
                char c = (char) buffer[p++];
                if (c == 0) {
                    break;
                } else { // got a valid character
                    if (sb == null) sb = new StringBuilder(siz);
                    sb.append(c);
                }
            }
            return (sb == null) ? null : sb.toString();
        } else {
            return ((GridPageX<D>) page).getString(index, off);
        }
    }

    void putAscii(int off, String v, int len) {
        int p = schema().size * index + off;
        int min = Math.min(len, v.length());
        for (int i = 0; i < min; i++) {
            char c = v.charAt(i);
            buffer[p++] = (byte) (c & 0xff);
        }
    }

    byte[] getBinary(int off, int len) {
        if (buffer != null) {
            int p = schema().size * index + off;
        } else {

        }
        return null;
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
