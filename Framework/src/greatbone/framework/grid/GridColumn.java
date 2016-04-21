package greatbone.framework.grid;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The structural information about a data column that is declared in a data entry class..
 */
abstract class GridColumn<T> {

    int ordinal;

    // lowercase name
    String key;

    // quoted name, used in database
    String qname;

    int origin;

    int offset;

    final String name() {
        return key;
    }

    final String qname() {
        return qname;
    }

    void INIT(int ordinal, String name, int offset) {
        this.ordinal = ordinal;
        this.key = name.toLowerCase();
        this.qname = "\"" + name + "\"";
        this.offset = offset;
    }

    abstract int size();

    abstract void load(GridData dat, ResultSet rs) throws SQLException;

    abstract void param(GridData dat, PreparedStatement pstmt) throws SQLException;

}
