package greatbone.framework.grid;

import greatbone.framework.Out;
import greatbone.framework.Printer;
import greatbone.framework.web.WebContext;

import java.io.IOException;

/**
 * The superclass for all strong-typed data entry classes. A data entry points to the attached byte buffer block and acts as a cursor.
 * <p/>
 * Two cases
 * 1) based on memory page store, when used internally
 * 2) based on isolated buffer, used in public API
 */
public abstract class GridData<T extends GridData<T>> implements Printer {

    // schema information
    GridSchema schema;

    // the backing local page
    final GridLocalPage page;

    // the backing byte array
    int[] buckets; // hash buckets
    final byte[] array;

    // current index
    int index;

    public GridData() {
        this(1);
    }

    public GridData(int entries) {
        this.page = null;
        // initialize the backing byte array
        GridSchema<T> sch = schema();
        this.array = new byte[sch.size * entries];
        this.index = 0;
    }

    GridData(GridLocalPage page, int index) {
        this.page = page;
        this.index = index;

        this.array = null;
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

    protected abstract GridSchema<T> schema();

    public void save(WebContext x) {
    }

    short getShort(int off) {
        if (page != null) {
            return page.eshort(index, off);
        } else {
            int p = schema.size * index + off;
            return (short) ((array[p++] << 8) + array[p]);
        }
    }

    void putShort(int off, short v) {
        int p = schema.size * index + off;
        array[p++] = (byte) ((v >>> 8) & 0xff);
        array[p] = (byte) (v & 0xff);
    }

    int getInt(int off) {
        if (page != null) {
            return page.eint(index, off);
        } else {
            int p = schema.size * index + off;
            return array[p++] + (array[p++] << 8) + (array[p++] << 16) + (array[p] << 24);
        }
    }

    void putInt(int off, int v) {
        int p = schema.size * index + off;
        array[p++] = (byte) (v & 0xff);
        array[p++] = (byte) ((v >>> 8) & 0xff);
        array[p++] = (byte) ((v >>> 16) & 0xff);
        array[p] = (byte) ((v >>> 24) & 0xff);
    }

    String getString(int off) {
        if (page != null) {
            return page.estring(index, off);
        } else {
            StringBuilder sb = null;
            int p = schema.size * index + off;
            while (p < schema.size) {
                char c = (char) ((array[p++] << 8) + array[p++]);
                if (c == 0) {
                    break;
                } else { // got a valid character
                    if (sb == null) sb = new StringBuilder(schema.size);
                    sb.append(c);
                }
            }
            return (sb == null) ? null : sb.toString();
        }
    }

    void putString(int off, String v) {
        int p = schema.size * index + off;
        for (int i = 0; i < v.length(); i++) {
            char c = v.charAt(i);
            array[p++] = (byte) ((c >>> 8) & 0xff);
            array[p++] = (byte) (c & 0xff);
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

}
