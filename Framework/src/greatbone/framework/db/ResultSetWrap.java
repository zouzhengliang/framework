package greatbone.framework.db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A wrap over a JDBC resultset.
 */
public class ResultSetWrap {

    final ResultSet rs;

    // indicate the current index under getting
    int p;

    ResultSetWrap(ResultSet rs) {
        this.rs = rs;
    }

    public short short_() throws SQLException {
        return rs.getShort(++p);
    }

    public int int_() throws SQLException {
        return rs.getInt(++p);
    }

    public String str() throws SQLException {
        return rs.getString(++p);
    }

}
