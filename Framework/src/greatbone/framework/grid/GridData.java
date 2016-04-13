package greatbone.framework.grid;

import greatbone.framework.Out;
import greatbone.framework.Printer;
import greatbone.framework.web.WebContext;

import java.io.IOException;

/**
 * The superclass for all strong-typed data entry classes. A data entry points to the attached byte buffer block and acts as a cursor.
 * <p/>
 * Two cases
 * 1) based on native memory page store, when used internally
 * 2) based on on-heap buffer, used in public API
 * <p/>
 * A data entry has a string key
 *
 * @param <D> type of this object
 */
public abstract class GridData<D extends GridData<D>> implements Printer {

    // the backing local page
    GridLocalPage<D> page;

    // the backing byte array
    int[] buckets; // hash buckets
    byte[] buffer;

    // current index
    int index;

    void init(int entries) {
        this.buffer = new byte[schema().size * entries];
    }

    void init(GridLocalPage<D> page) {
        this.page = page;
    }

    public int hash() {
        return 0;
    }

    public int next() {
        return 0;
    }

    public void parse(WebContext wc) {
        // parse data according to schema


    }

    public void save(WebContext x) {
    }

    short getShort(int off) {
        if (page != null) {
            return page.eshort(index, off);
        } else {
            int p = schema().size * index + off;
            return (short) ((buffer[p++] << 8) + buffer[p]);
        }
    }

    void putShort(int off, short v) {
        int p = schema().size * index + off;
        buffer[p++] = (byte) ((v >>> 8) & 0xff);
        buffer[p] = (byte) (v & 0xff);
    }

    int getInt(int off) {
        if (page != null) {
            return page.eint(index, off);
        } else {
            int p = schema().size * index + off;
            return buffer[p++] + (buffer[p++] << 8) + (buffer[p++] << 16) + (buffer[p] << 24);
        }
    }

    void putInt(int off, int v) {
        int p = schema().size * index + off;
        buffer[p++] = (byte) (v & 0xff);
        buffer[p++] = (byte) ((v >>> 8) & 0xff);
        buffer[p++] = (byte) ((v >>> 16) & 0xff);
        buffer[p] = (byte) ((v >>> 24) & 0xff);
    }

    String getString(int off, int len) {
        int siz = schema().size;
        if (page != null) {
            return page.estring(index, off);
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

    String getAscii(int off, int len) {
        int siz = schema().size;
        if (page != null) {
            return page.estring(index, off);
        } else {
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
