package greatbone.framework.grid;

import sun.misc.Unsafe;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * A JVM-local data page store that resides off-heap.
 */
class GridLocalPage<K, D extends GridData<D>> extends GridPage<K, D> implements GridLocalPageMBean {

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

    // length of each data entry. long integer so that related arithmetics results are long
    final long entrylen;

    // address of the OFF-HEAP data store
    final long store;

    // actual data entries
    volatile int count;

    GridLocalPage(GridDataSet<K, D> parent, K id, int capacity) {
        super(parent, id);

        this.entrylen = parent.schema.size;

        // initialize the off-heap data store
        int cap = 1;
        while (cap < capacity) { // ensure power of 2
            cap <<= 1;
        }
        buckets = new AtomicIntegerArray(cap);
        for (int i = 0; i < cap; i++) {
            buckets.set(i, -1); // initialize all buckets to -1
        }
        store = UNSAFE.allocateMemory(entrylen * cap);
        count = 0;

        // register as mbean
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName objname = new ObjectName(":type=LocalPage");
            mbs.registerMBean(this, objname);
        } catch (Exception e) {
        }

    }


    void enterRead(int index) {
        long sync = store + entrylen * index; // offset of the sync flag
        int prev;
        for (; ; ) {
            prev = UNSAFE.getIntVolatile(null, sync); // first byte is the sync flag
            if ((prev >> 24) >= 0 && UNSAFE.compareAndSwapInt(null, sync, prev, prev + (1 << 24))) {
                return; // the only return point is after increment
            }
            elapse();
        }
    }

    void exitRead(int index) {
        long sync = store + entrylen * index; // offset of the sync flag
        int prev;
        for (; ; ) {
            prev = UNSAFE.getIntVolatile(store, sync); // first byte is the sync flag
            if ((prev >> 24) > 0 && UNSAFE.compareAndSwapInt(null, sync, prev, prev - (1 << 24))) {
                return; // the only return point is after increment
            }
            elapse();
        }
    }

    void enterWrite(int index) {
        long sync = store + entrylen * index; // offset of the sync flag
        int prev;
        for (; ; ) {
            prev = UNSAFE.getIntVolatile(store, sync); // first byte is the sync flag
            if ((prev >> 24) == 0 && UNSAFE.compareAndSwapInt(null, sync, prev, prev & (-11 << 24))) {
                return; // the only return point is after increment
            }
            elapse();
        }
    }

    void exitWrite(int index) {
        long sync = store + entrylen * index; // offset of the sync flag
        int prev;
        for (; ; ) {
            prev = UNSAFE.getIntVolatile(store, sync); // first byte is the sync flag
            if ((prev >> 24) == 0 && UNSAFE.compareAndSwapInt(null, sync, prev, prev & (-11 << 24))) {
                return; // the only return point is after increment
            }
            elapse();
        }
    }

    // elapse a number of cycles
    void elapse() {
        for (int i = 0; i < 32; i++) ;
    }

    @Override
    D get(K key) {
        int code = key.hashCode() & 0x7fffffff;
        int idx = buckets.get(code % buckets.length());
        while (idx != -1) {
            if (code == ecode(idx) && ekey(idx, key)) { // test hash-plus-key equality
                D data = parent.create();
//                data.page = this;
//                data.index = idx;
                ecopyto(idx, data);
                return data;
            }
            idx = enext(idx);
        }
        return null;
    }

    @Override
    K put(K key, D data) {
        int code = key.hashCode() & 0x7fffffff;
        int bucket = code % buckets.length(); // target bucket
        int idx = buckets.get(bucket);
        while (idx != -1) {
            if (code == ecode(idx) && ekey(idx, key)) { // if exist then copy to replace the value
                ecopyfrom(idx, data);
                return key;
            }
            idx = enext(idx); // to next index
        }
        // add a new entry
        idx = count;
        ecopyfrom(idx, data);
        ecode(idx, code); // set hash code
        enext(idx, -1); // set next index
        buckets.set(bucket, idx);
        count++;
        return key;
    }

    // get entry hash code
    int ecode(int index) {
        long addr = store + entrylen * index + 4;
        return UNSAFE.getIntVolatile(null, addr);
    }

    // set entry's hash code item
    void ecode(int index, int v) {
        long addr = store + entrylen * index + 4;
        UNSAFE.putIntVolatile(null, addr, v);
    }

    int enext(int index) {
        long addr = store + entrylen * index + 8;
        return UNSAFE.getIntVolatile(null, addr);
    }

    // set entry's next item
    void enext(int index, int v) {
        long addr = store + entrylen * index + 8;
        UNSAFE.putIntVolatile(null, addr, v);
    }

    boolean ekey(int index, K key) {
        long addr = store + entrylen * index + 12;
        if (key instanceof String) {
            String s = (String) key;
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c != UNSAFE.getChar(addr)) {
                    return false;
                }
            }
            return true;
        } else if (key instanceof Integer) {
            return (Integer) key == UNSAFE.getInt(addr);
        } else if (key instanceof Long) {
            return (Long) key == UNSAFE.getLong(addr);
        } else if (key instanceof Short) {
            return (Short) key == UNSAFE.getShort(addr);
        }
        return false;
    }

    void ecopyto(int index, D data) {
        long addr = store + entrylen * index;
        UNSAFE.copyMemory(null, addr, data.array, 16, entrylen);
    }

    void ecopyfrom(int index, D data) {
        long addr = store + entrylen * index;
        UNSAFE.copyMemory(data.array, 16, null, addr, entrylen);
    }

    short eshort(int index, int off) {
        long addr = store + entrylen * index + off;
        return UNSAFE.getShort(addr);
    }

    int eint(int index, int off) {
        long addr = store + entrylen * index + off;
        return UNSAFE.getInt(addr);
    }

    String estring(int index, int off) {
        StringBuilder sb = null;
        long addr = store + entrylen * index + off;
        while (addr < entrylen) {
            char c = UNSAFE.getChar(addr);
            if (c == 0) {
                break;
            } else { // got a valid character
                if (sb == null) {
                    sb = new StringBuilder((int) entrylen);
                }
                sb.append(c);
                addr += 2;
            }
        }
        return (sb == null) ? null : sb.toString();
    }


    D search(Critera<D> filter) {
        D dat = parent.create();

        int i = 0;
        while (i < count) {
            //
        }
        return null;
    }


    @Override
    protected void finalize() throws Throwable {
        UNSAFE.freeMemory(store);
        super.finalize();
    }

}
