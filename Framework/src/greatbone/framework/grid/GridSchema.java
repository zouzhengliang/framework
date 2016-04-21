package greatbone.framework.grid;

import greatbone.framework.util.Roll;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The meta information for a data entry.
 *
 * @param <D> data object
 */
public class GridSchema<D extends GridData<D>> {

    // each data entry reserves 12 leading bytes for controlling purposes (FLAGS, HASH, NEXT)
    static final int RESERVED = 12;

    // the default data object constructor
    final Constructor<D> ctor;

    // definitions of regular columns
    final Roll<String, GridColumn> columns = new Roll<>(64);
    final KEY keycol; // definition of the key column

    // total bytes of a data record, including those reserved
    final int size;

    // SQL clause
    String select, insert, update, delete;

    public GridSchema(Class<D> datclass) {

        // resolve the data class by type parameter
        try {
            this.ctor = datclass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new GridSchemaException(e.getMessage());
        }

        // collect column declarations

        KEY keycol = null;
        int offset = RESERVED;
        for (final Field fld : datclass.getDeclaredFields()) {
            Class<?> type = fld.getType();
            int mod = fld.getModifiers();
            if (Modifier.isStatic(mod)) {
                GridColumn col = null;
                try {
                    if (GridColumn.class.isAssignableFrom(type)) {
                        fld.setAccessible(true);
                        col = (GridColumn) fld.get(null);
                    } else if (type.isArray() && STRUCT[].class.isAssignableFrom(type)) {
                        fld.setAccessible(true);
                        STRUCT[] structs = (STRUCT[]) fld.get(null);
                        col = new GridStructsColumn(structs);
                    }
                } catch (IllegalAccessException e) { // never happen
                }
                if (col != null) {
                    // set late column attributes
                    col.INIT(1, fld.getName().toLowerCase(), offset);
                    offset += col.size();
                    columns.put(col.key, col);
                    // get key column
                    if (keycol == null && col instanceof KEY) {
                        keycol = (KEY) col;
                    }
                }
            }
        }

        this.keycol = keycol;
        // total length
        this.size = offset;

        this.select = evalSelectClause();
    }

    final Roll<String, GridColumn> columns() {
        return columns;
    }

    void load(GridData<D> dat, ResultSet rs) throws SQLException {
        for (int i = 0; i < columns.count(); i++) {
            GridColumn col = columns.get(i);
            col.load(dat, rs);
        }
    }

    D instantiate() {
        try {
            return ctor.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    String evalSelectClause() {
        StringBuilder sb = new StringBuilder("SELECT ");
        for (int i = 0; i < columns.count(); i++) {
            GridColumn col = columns.get(i);
            if (i > 0) {
                sb.append(",");
            }
            sb.append(col.key);
        }
        return sb.toString();
    }

}
