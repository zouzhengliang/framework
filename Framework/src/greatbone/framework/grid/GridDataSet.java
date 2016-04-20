package greatbone.framework.grid;

import greatbone.framework.db.DbContext;
import greatbone.framework.util.Roll;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * A data collection of particular data class.
 * <p/>
 * pertain to a database table, act as a hash map
 * <p/>
 * consists of a number of partitions, predefined through config.xml
 * <p/>
 * setup during environment initialization
 */
public abstract class GridDataSet<D extends GridData<D>> extends GridSet implements GridDataSetMBean {

    // the data schema
    final GridSchema<D> schema;

    // annotated cache policy, can be null
    final CachePolicy cachepol;

    // primary data pages, both origins and references
    final GridPages<D> primary;

    // the backup copy of the preceding node's origin data pages
    final GridPages<D> copy;

    @SuppressWarnings("unchecked")
    protected GridDataSet(GridUtility grid, int inipages) {
        super(grid);

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
        this.cachepol = getClass().getAnnotation(CachePolicy.class);

        this.primary = new GridPages<>(inipages);
        this.copy = new GridPages<>(inipages);

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

    public D newData() {
        return schema.instantiate();
    }

    //
    // PAGE OPERATIONS

    public GridPage<D> getPage(String pageid) {
        return primary.get(pageid);
    }

    public GridPage<D> locatePage(String datakey) {
        return primary.locate(datakey);
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

    protected void load() {

        // compose sql statement
        StringBuilder sql = new StringBuilder(schema.select);
        sql.append(" FROM ").append(key);

        String likes;
        if (localspec != null) {
            for (int i = 0; i < localspec.size(); i++) {
                String con = localspec.get(i);
                sql.append(schema.keycol.name).append(" LIKE ").append(con);
                if (i != localspec.size() - 1) {
                    sql.append(" OR ");
                }
            }
            likes = sql.toString();
        } else {
            likes = null;
        }

        sql.append("WHERE ").append(likes);

        String filter;
        if (config != null && !(filter = config.getAttribute("filter")).isEmpty())
            sql.append(" AND (").append(filter).append(")");

        try (DbContext dc = new DbContext()) {
            dc.query(sql.toString(), null, wrap -> {

//                wrap.
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    void loadwith(ResultSet rs) throws SQLException {
        D dat = schema.instantiate();

        while (rs.next()) {

        }
    }



    /**
     * Gets a single specified data entry.
     *
     * @param key the data entry to find
     * @return a data object containing a single entry, or null
     */
    public D getData(String key) {
        // locate the page
        GridPage<D> page = locatePage(key);
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
    public D getData(String... keys) {
        List<GridGet<D>> tasks = null;
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            GridPage<D> page = locatePage(key);
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

    public D getData(Critera<D> d) {
        return null;
    }

    // a subclass may treat key differently, it can be full key, partial key, or null
    public D put(String key, D dat) {
        if (key == null) {

        }
        // find the target page
        GridPage<D> page = locatePage(key);
        if (page == null) {
            page = new GridPageX<>(this, null, 1024);
            primary.add(page);
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

}
