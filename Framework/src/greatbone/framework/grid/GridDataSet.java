package greatbone.framework.grid;

import greatbone.framework.Config;
import greatbone.framework.Greatbone;
import greatbone.framework.db.DbContext;
import greatbone.framework.util.Roll;
import org.w3c.dom.Element;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.sql.SQLException;
import java.util.*;

/**
 * A data collection of particular data class.
 * <p/>
 * pertain to a database table, act as a hash map
 * <p/>
 * consists of a number of partitions, predefined through config.xml
 * <p/>
 * setup during environment initialization
 */
public abstract class GridDataSet<D extends GridData<D>> extends GridSet implements GridDataSetMBean, Config {

    // the data schema
    final GridSchema<D> schema;

    // annotated cache policy, can be null
    CachePolicy cachepol;

    // orgin data pages of this dataset which reside on this node
    GridPages<D> origin;

    // backup of the preceding node
    GridPages<D> backup;

    // configuration xml element
    final Element config;

    @SuppressWarnings("unchecked")
    protected GridDataSet(GridUtility grid, int inipages) {

        super(grid);

        this.config = Greatbone.childOf(grid.config, "dataset", key);

        Class<D> datc = (Class<D>) typearg(0); // resolve the data class by type parameter
        this.schema = grid.schema(datc);
        // register mbean
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName objname = new ObjectName("grid.dataset:type=DataSet,name=" + key);
            mbs.registerMBean(this, objname);
        } catch (Exception e) {
        }

        // prepare page table

        this.origin = new GridPages<>(inipages);

    }

    // resolve a type argument along the inheritance hierarchy
    final Class typearg(int ordinal) {
        // gather along the inheritence hierarchy
        Deque<Class> que = new LinkedList<Class>();
        for (Class c = getClass(); c != GridDataSet.class; c = c.getSuperclass()) {
            que.addFirst(c);
        }
        for (Class c : que) {
            Type t = ((ParameterizedType) c.getGenericSuperclass()).getActualTypeArguments()[ordinal];
            if (t instanceof Class) {
                return (Class) t;
            } else {
                String var = ((TypeVariable) t).getName();
                Type[] cts = c.getTypeParameters();
                int p = -1;
                for (int i = 0; i < cts.length; i++) { // locate the var name in class' parameter list
                    if (((TypeVariable) cts[i]).getName().equals(var)) {
                        p = i;
                        break;
                    }
                }
                if (p == -1) {
                    throw new GridSchemaException("type variable");
                } else {
                    ordinal = p;
                }
            }
        }
        return null;
    }

    public String key() {
        return key;
    }

    @Override
    public void flush() {

    }

    public D instantiate() {
        return schema.instantiate();
    }

    //
    // PAGE OPERATIONS

    GridPage<D> locate(int index) {
        return null;
    }

    GridPage<D> resolve(String key) {
        return null;
    }

    String select(String condition) {
        Roll<String, GridColumn> cols = schema.columns;
        StringBuilder sb = new StringBuilder("SELECT ");
        for (int i = 0; i < cols.size(); i++) {
            GridColumn col = cols.get(i);
            sb.append(col.name);
        }
        sb.append(" FROM ");
        sb.append(key);
        sb.append(" WHERE ");
        sb.append(condition);
        return sb.toString();
    }

    String update() {
        Roll<String, GridColumn> cols = schema.columns;
        StringBuilder sb = new StringBuilder("UPDATE ").append(key).append(" SET ");
        for (int i = 0; i < cols.size(); i++) {
            GridColumn col = cols.get(i);
            sb.append(col.name).append("=?");
        }
        return sb.toString();
    }

    protected void load(String arg) {

        // create shards based on configuration
        String attlocal = config.getAttribute("local");
        StringTokenizer st = new StringTokenizer(attlocal, ",");
        while (st.hasMoreTokens()) {
            String tok = st.nextToken().trim();

//            GridShard<K, V> p = new GridShard<>(this, tok, 1024);

        }


        // after init of shards
        try (DbContext ctx = new DbContext()) {
            ctx.update("SELECT * FROM " + key + " WHERE ", null);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Gets a single specified data entry.
     *
     * @param key the data entry to find
     * @return a data object containing a single entry, or null
     */
    public D get(String key) {
        // locate the page
        GridPage<D> page = resolve(key);
        if (page != null) {
            return page.get(key);
        }
        return null;
    }

    /**
     * Gets a number of data entries specified by an array of keys. The gets will execute in parallel.
     *
     * @param keys data entries to find
     * @return an merged data object, or null
     */
    public D get(String... keys) {
        List<GridGet<D>> tasks = null;
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            GridPage<D> page = resolve(key);
            if (page != null) {
                if (tasks == null) tasks = new ArrayList<>(keys.length); // lazy creation of task list
                tasks.add(new GridGet<>(page, key));
            }
        }
        if (tasks != null) {
            try {
                GridGet.invokeAll(tasks);
                // harvest the results
                D merge = null;
                for (int i = 0; i < tasks.size(); i++) {
                    D res = tasks.get(i).result;
                    if (res != null) {
                        if (merge == null) {
                            merge = res;
                        } else {
                            merge.add(res);
                        }
                    }
                }
                return merge;
            } catch (Exception e) {
            }
        }
        return null;
    }

    public D getAll(Critera<D> d) {
        return null;
    }

    // a subclass may treat key differently, it can be full key, partial key, or null
    public D put(String key, D dat) {
        if (key == null) {

        }
        // find the target page
        GridPage<D> page = resolve(key);
        if (page == null) {
            page = new GridPageX<>(this, null, 1024);
            origin.insert(page);
        }
        page.put(key, dat);
        return dat;
    }

    public void forEach(Critera<D> condition) {

    }

    public void clear() {

    }

    public void close() {

    }

    public boolean isClosed() {
        return false;
    }

    @Override
    public Element config() {
        return config;
    }

}
