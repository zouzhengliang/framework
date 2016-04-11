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
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A data collection of particular data class.
 * <p/>
 * pertain to a database table, act as a hash map
 * <p/>
 * consists of a number of partitions, predefined through config.xml
 * <p/>
 * setup during environment initialization
 */
public abstract class GridDataSet<K, D extends GridData<D>> implements Fabric, GridDataSetMBean, Config {

    final GridUtility parent;

    final String key;

    // the data schema
    final GridSchema<D> schema;

    // can be null
    ReadPolicy rpolicy;

    GridPageList<K, D> primary;

    // copy of neighbor
    GridPageList<K, D> copy;

    // configuration xml element
    final Element config;

    @SuppressWarnings("unchecked")
    protected GridDataSet(GridUtility grid, int inipages) {

        this.key = getClass().getSimpleName().toLowerCase(); // from class name
        this.config = Greatbone.childOf(grid.config, "dataset", key);

        this.parent = grid;
        Class<D> datc = (Class<D>) typearg(1); // resolve the data class by type parameter
        this.schema = grid.schema(datc);
        // register mbean
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName objname = new ObjectName("grid.dataset:type=DataSet,name=" + key);
            mbs.registerMBean(this, objname);
        } catch (Exception e) {
        }

        // prepare page table

        this.primary = new GridPageList<>(inipages);

    }

    // resolve a type argument along the inheritance hierarchy
    Class typearg(int ordinal) {
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

    public D create() {
        return schema.newObj();
    }

    //
    // PAGE OPERATIONS

    GridPage<K, D> shard(int index) {
        return null;
    }

    @SuppressWarnings("unchecked")
    void insert(GridPage<K, D> page) {

    }

    abstract GridPage<K, D> shard(K key);

    public List<GridQuery<K, D>> query(Critera<K> keyer, Critera<D> filter) {

        return primary.query(keyer, filter);
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

    public D create(K key) {
        return null;
    }

    public D get(K key) {
        // find the target page
        GridPage<K, D> page = shard(key);
        if (page != null) {
            return page.get(key);
        }
        return null;
    }

    public D getAll(Critera<D> d) {

        return null;
    }


    // a subclass may treat key differently, it can be full key, partial key, or null
    public K put(K key, D data) {
        if (key == null) {

        }
        // find the target page
        GridPage<K, D> page = shard(key);
        if (page == null) {
            page = new GridLocalPage<>(this, null, 1024);
            insert(page);
        }
        page.put(key, data);
        return key;
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
