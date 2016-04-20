package greatbone.framework.grid;

import sun.misc.Unsafe;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * A native (origin) data page that resides in off-heap memory.
 */
class GridPageX<D extends GridData<D>> extends GridPage<D> implements GridPageMBean {

    // for atomic operation
    static final Unsafe UNSAFE;

    static {
        try { // a common trick to get HotSpot's internal Unsafe
            Field fld = Unsafe.class.getDeclaredField("theUnsafe");
            fld.setAccessible(true);
            UNSAFE = (Unsafe) fld.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new Error(e);
        }
    }

    // hash buckets
    volatile AtomicIntegerArray buckets;

    // length of each data record, long arithmetics
    final long recSize;

    // address of the OFF-HEAP data buffer
    final long content;

    // actual number of data records
    volatile int count;

    GridPageX(GridDataSet<D> parent, String id, int capacity) {
        super(parent, id);

        this.recSize = parent.schema.size;

        // initialize the off-heap data buffer
        int cap = 1;
        while (cap < capacity) { // ensure power of 2
            cap <<= 1;
        }
        buckets = new AtomicIntegerArray(cap);
        for (int i = 0; i < cap; i++) {
            buckets.set(i, -1); // initialize all buckets to -1
        }
        content = UNSAFE.allocateMemory(recSize * cap);
        count = 0;

        // register as mbean
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName objname = new ObjectName(":type=Page");
            mbs.registerMBean(this, objname);
        } catch (Exception e) {
        }

    }

    void enterRead(int index) {
        long sync = content + recSize * index; // offset of the sync flag
        int prev;
        for (; ; ) {
            prev = UNSAFE.getIntVolatile(null, sync); // first byte is the sync flag
            if ((prev >> 24) >= 0 && UNSAFE.compareAndSwapInt(null, sync, prev, prev + (1 << 24))) {
                return; // the only return point is after increment
            }
            loop();
        }
    }

    void exitRead(int index) {
        long sync = content + recSize * index; // offset of the sync flag
        int prev;
        for (; ; ) {
            prev = UNSAFE.getIntVolatile(content, sync); // first byte is the sync flag
            if ((prev >> 24) > 0 && UNSAFE.compareAndSwapInt(null, sync, prev, prev - (1 << 24))) {
                return; // the only return point is after increment
            }
            loop();
        }
    }

    void enterWrite(int index) {
        long sync = content + recSize * index; // offset of the sync flag
        int prev;
        for (; ; ) {
            prev = UNSAFE.getIntVolatile(content, sync); // first byte is the sync flag
            if ((prev >> 24) == 0 && UNSAFE.compareAndSwapInt(null, sync, prev, prev & (-11 << 24))) {
                return; // the only return point is after increment
            }
            loop();
        }
    }

    void exitWrite(int index) {
        long sync = content + recSize * index; // offset of the sync flag
        int prev;
        for (; ; ) {
            prev = UNSAFE.getIntVolatile(content, sync); // first byte is the sync flag
            if ((prev >> 24) == 0 && UNSAFE.compareAndSwapInt(null, sync, prev, prev & (-11 << 24))) {
                return; // the only return point is after increment
            }
            loop();
        }
    }

    // elapse a number of cycles
    void loop() {
        for (int i = 0; i < 16; i++) ;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public D get(String key) {
        int code = key.hashCode() & 0x7fffffff;
        int idx = buckets.get(code % buckets.length());
        while (idx != -1) {
            if (code == ecode(idx) && ekey(idx, key)) { // test hash-plus-key equality
                D dat = parent.newData();
                dat.page = this;
                dat.index = idx;
                ecopyto(idx, dat);
                return dat;
            }
            idx = enext(idx);
        }
        return null;
    }

    @Override
    public D put(String key, D dat) {
        int code = key.hashCode() & 0x7fffffff;
        int bucket = code % buckets.length(); // target bucket
        int idx = buckets.get(bucket);
        while (idx != -1) {
            if (code == ecode(idx) && ekey(idx, key)) { // if exist then copy to replace the value
                rcopy(idx, dat);
                return dat;
            }
            idx = enext(idx); // to next index
        }
        // add a new entry
        idx = count;
        rcopy(idx, dat);
        ecode(idx, code); // set hash code
        enext(idx, -1); // set next index
        buckets.set(bucket, idx);
        count++;
        return dat;
    }

    public D search(Critera<D> filter) {
        D dat = parent.newData();
        int i = 0;
        while (i < count) {
            //
        }
        return null;
    }

    void pack() {

    }

    // get entry hash code
    int ecode(int index) {
        long addr = content + recSize * index + 4;
        return UNSAFE.getIntVolatile(null, addr);
    }

    // set entry's hash code item
    void ecode(int index, int v) {
        long addr = content + recSize * index + 4;
        UNSAFE.putIntVolatile(null, addr, v);
    }

    int enext(int index) {
        long addr = content + recSize * index + 8;
        return UNSAFE.getIntVolatile(null, addr);
    }

    // set entry's next item
    void enext(int index, int v) {
        long addr = content + recSize * index + 8;
        UNSAFE.putIntVolatile(null, addr, v);
    }

    boolean ekey(int index, String key) {
        long addr = content + recSize * index + 12;
        String s = (String) key;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c != UNSAFE.getChar(addr)) {
                return false;
            }
        }
        return true;
    }

    void ecopyto(int index, D data) {
        long addr = content + recSize * index;
        UNSAFE.copyMemory(null, addr, data.buffer, 16, recSize);
    }

    void rcopy(int index, D data) {
        long addr = content + recSize * index;
        UNSAFE.copyMemory(data.buffer, 16, null, addr, recSize);
    }

    short getShort(int index, int off) {
        long addr = content + recSize * index + off;
        return UNSAFE.getShort(addr);
    }

    void putShort(int index, int off, short v) {
        long addr = content + recSize * index + off;
        UNSAFE.putShort(addr, v);
    }

    int getInt(int index, int off) {
        long addr = content + recSize * index + off;
        return UNSAFE.getInt(addr);
    }

    void putInt(int index, int off, int v) {
        long addr = content + recSize * index + off;
        UNSAFE.putInt(addr, v);
    }

    String getString(int index, int off) {
        StringBuilder sb = null;
        long addr = content + recSize * index + off;
        int i = 0;
        while (i < recSize) {
            char c = UNSAFE.getChar(i);
            if (c == 0) {
                break;
            } else { // got a valid character
                if (sb == null) {
                    sb = new StringBuilder((int) recSize);
                }
                sb.append(c);
                i += 2;
            }
        }
        return (sb == null) ? null : sb.toString();
    }

    int compareString(int index, int off, String v) {
        long addr = content + recSize * index + off;
        return -1;
    }

    void putString(int index, int off, String v) {
        long addr = content + recSize * index + off;
        for (int i = 0; i < v.length(); i = i + 2) {
            UNSAFE.putChar(addr, v.charAt(i));
        }
    }

    BigDecimal getDecimal(int index, int off) {
        long addr = content + recSize * index + off;
        return null;
    }

    int compareDecimal(int index, int off, BigDecimal v) {
        long addr = content + recSize * index + off;
        return -1;
    }

    void putDecimal(int index, int off, BigDecimal v) {
        long addr = content + recSize * index + off;
    }

    @Override
    protected void finalize() throws Throwable {
        UNSAFE.freeMemory(content);
        super.finalize();
    }

}
