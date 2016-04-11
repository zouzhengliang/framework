package greatbone.framework.grid;

import greatbone.framework.Config;
import greatbone.framework.Greatbone;
import greatbone.framework.util.SpinWait;
import org.w3c.dom.Element;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * cache of underlying local file system files. off-heap memory allocation
 */
abstract class GridFileSet implements Fabric, Config {

    final GridUtility parent;

    final String key;

    final Element config;

    final SpinWait sync = new SpinWait();
    GridFolder[] shards;
    int count;

    @SuppressWarnings("unchecked")
    protected GridFileSet(GridUtility parent, int capacity) {

        this.key = getClass().getSimpleName().toLowerCase(); // from class name

        this.config = Greatbone.childOf(parent.config, "fileset", key);

        this.parent = parent;
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

    @Override
    public Element config() {
        return config;
    }

}
