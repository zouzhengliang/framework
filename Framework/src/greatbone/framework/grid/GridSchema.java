package greatbone.framework.grid;

import greatbone.framework.util.Roll;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 * The meta information for a data entry.
 *
 * @param <D> data object
 */
public class GridSchema<D extends GridData<D>> {

    // each data entry reserves 12 leading bytes for controlling purposes (FLAGS, HASH, NEXT)
    static final int RESERVED = 12;

    static final String KEY_COL = "key";

    // the default data object constructor
    final Constructor<D> ctor;

    // definition of the key column
    final KEY keycol;

    // definitions of regular columns
    final Roll<String, GridColumn> columns = new Roll<>(64);

    // total size of a data entry, including reserved bytes
    final int size;

    // SQL statements
    String select, insert, update, delete;

    public GridSchema(Class<D> datc, int keylen) {

        // resolve the data class by type parameter
        try {
            this.ctor = datc.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new GridSchemaException(e.getMessage());
        }

        KEY keycol = new KEY(keylen);
        keycol.init(KEY_COL, RESERVED);
        this.keycol = keycol;

        // collect column declarations

        int offset = keycol.tail();
        for (final Field fld : datc.getDeclaredFields()) {
            Class<?> type = fld.getType();
            int mod = fld.getModifiers();
            if (Modifier.isStatic(mod) && GridColumn.class.isAssignableFrom(type)) {
                fld.setAccessible(true);
                GridColumn col = null;
                try {
                    col = (GridColumn) fld.get(null);
                } catch (IllegalAccessException e) { // never happen
                    e.printStackTrace();
                }
                if (col != null) {
                    // set late column attributes
                    col.init(fld.getName().toLowerCase(), offset);
                    offset += col.size();

                    columns.put(col.name, col);
                }
            }
        }
        // total length
        this.size = offset;
    }

    D newObj() {
        try {
            return ctor.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    D newObj(int entries) {
        try {
            return ctor.newInstance(entries);
        } catch (InstantiationException | IllegalAccessException e) {
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
