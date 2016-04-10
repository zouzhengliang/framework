package greatbone.framework.grid;

import greatbone.framework.util.Roll;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 * The structural information for a data entry.
 */
public class GridSchema<D extends GridData<D>> {

    // each data entry reserves 12 leading bytes for controlling purposes (FLAGS, HASH, NEXT)
    static final int RESERVED = 12;

    // the default data object constructor, for a single entry
    final Constructor<D> ctor;

    // the default data object constructor, for a specified number of entries
//    final Constructor<D> ctorn;

    // definitions of columns
    final Roll<String, GridColumn> columns = new Roll<>(64);

    // total size of a data entry, including reserved bytes
    final int size;

    // SQL statements
    String select, insert, update, delete;

    public GridSchema(Class<D> datc) {

        // resolve the data class by type parameter
        try {
            this.ctor = datc.getConstructor();
//            this.ctorn = datclass.getConstructor(Integer.class);
        } catch (NoSuchMethodException e) {
            throw new GridSchemaException(e.getMessage());
        }

        int offset = RESERVED;

        // collect column declaration fields
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
