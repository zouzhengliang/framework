package greatbone.framework.grid;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * cache of underlying local file system files. off-heap memory allocation
 */
public abstract class GridFileSet extends GridSet {

    public GridFileSet(GridUtility grid, int folders) {
        super(grid);

        // register mbean
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName objname = new ObjectName("myfarm.grid.fileset:type=FileSet,name=" + key);
            mbs.registerMBean(this, objname);
        } catch (Exception e) {
        }

    }

    public String key() {
        return key;
    }

    @Override
    public void flush() {

    }


}
