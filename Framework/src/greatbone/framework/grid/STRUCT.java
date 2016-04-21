package greatbone.framework.grid;

import greatbone.framework.util.Roll;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A column that itself consists of a number of sub columns.
 */
public abstract class STRUCT extends GridColumn {

    int length;

    final Roll<String, GridColumn> columns = new Roll<>(32);

    int size;

    void INIT(int ordinal, String name, int offset) {

        super.INIT(ordinal, name, offset);

        Class<? extends STRUCT> c = getClass();
        int off = offset;

        // iterate through fields
        for (final Field fld : c.getDeclaredFields()) {
            Class<?> type = fld.getType();
            int mod = fld.getModifiers();
            if (!Modifier.isStatic(mod) && GridColumn.class.isAssignableFrom(type)) {
                fld.setAccessible(true);
                GridColumn col = null;
                try {
                    col = (GridColumn) fld.get(this);
                } catch (IllegalAccessException e) { // never happen
                }
                if (col != null) {
                    // initialized column attributes
                    col.key = fld.getName().toLowerCase();
                    col.offset = off;
                    off += col.size();
                    columns.put(col.key, col);
                }
            }
        }

    }

    public String key() {
        return key;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    void load(GridData dat, ResultSet rs) throws SQLException {

    }

}
