package greatbone.framework.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A wrap over JDBC prepared statement.
 */
public class PreparedStatementWrap {

    final PreparedStatement pstmt;

    // indicate the current index under setting
    int p;

    PreparedStatementWrap(PreparedStatement pstmt) {
        this.pstmt = pstmt;
    }

    public PreparedStatementWrap set(int v) throws SQLException {
        pstmt.setInt(++p, v);
        return this;
    }

    public PreparedStatementWrap set(short v) throws SQLException {
        pstmt.setShort(++p, v);
        return this;
    }

    public PreparedStatementWrap set(String v) throws SQLException {
        pstmt.setString(++p, v);
        return this;
    }

}
