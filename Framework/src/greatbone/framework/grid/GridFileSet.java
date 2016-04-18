package greatbone.framework.grid;

import greatbone.framework.util.SpinWait;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * cache of underlying local file system files. off-heap memory allocation
 */
public abstract class GridFileSet extends GridSet {

    final SpinWait sync = new SpinWait();
    GridFolder[] shards;
    int count;

    @SuppressWarnings("unchecked")
    protected GridFileSet(GridUtility grid, int capacity) {

        super(grid);

        // register mbean
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName objname = new ObjectName("myfarm.grid.fileset:type=FileSet,name=" + key);
            mbs.registerMBean(this, objname);
        } catch (Exception e) {
        }

        shards = new GridFolder[capacity];

    }

    public String key() {
        return key;
    }

    @Override
    public void flush() {

    }

    GridFolder page(int index) {
        sync.enterRead();
        try {
            return shards[index];
        } finally {
            sync.exitRead();
        }
    }

    @SuppressWarnings("unchecked")
    void insert(GridFolder page) {
        sync.enterWrite();
        try {
            int len = shards.length;
            if (count == len) {
                GridFolder[] new_ = new GridFolder[len * 2];
                System.arraycopy(shards, 0, new_, 0, len);
                shards = new_;
            }
            shards[count++] = page;
        } finally {
            sync.exitWrite();
        }
    }

}
