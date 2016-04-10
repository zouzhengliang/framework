package greatbone.framework.db;

import java.sql.*;

/**
 * A data access session based on a data source. This class is not thread-safe.
 */
public class DbContext implements AutoCloseable {

    final DbSource source;

    Connection connection;

    public DbContext() {
        this(DbUtility.getSource());
    }

    public DbContext(String source) {
        this(DbUtility.getSource(source));
    }

    public DbContext(DbSource source) {
        this.source = source;
    }

    /**
     * Returns the connection to the pool.
     *
     * @param conn connection
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
            }
        }
    }

    public static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
                stmt = null;
            } catch (SQLException e) {
            }
        }
    }

    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                rs = null;
            } catch (SQLException e) {
            }
        }
    }

    /**
     * Submits a batch of commands to the database for execution and if all commands execute successfully, returns an array of update counts.
     *
     * @param statements SQL statements (typically INSERT or UPDATE) to be executed as a batch
     * @return an array of update counts containing one element for each command in the batch.  The elements of the array are ordered according to the order of statements argument
     * @throws SQLException if a database access error occurs, Throws {@link BatchUpdateException} (a subclass of <code>SQLException</code>) if one of the commands sent to the database fails to execute properly or attempts to return a result set.
     */
    public int[] batch(String[] statements) throws SQLException {
        if (statements == null || statements.length == 0) {
            return null;
        }
        if (connection == null) {
            connection = source.getConnection();
        }
        Statement stmt = connection.createStatement();
        try {
            for (String statement : statements) {
                stmt.addBatch(statement);
            }
            return stmt.executeBatch();
        } finally {
            close(stmt);
        }
    }

    /**
     * @param statement an SQL Data Manipulation Language (DML) statement, such as <code>INSERT</code>, <code>UPDATE</code> or <code>DELETE</code>; or an SQL statement that returns nothing, such as a DDL statement.
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements or (2) 0 for SQL statements that return nothing
     * @throws SQLException if a database access error occurs; this method is called on a closed  <code>PreparedStatement</code> or the SQL statement returns a <code>ResultSet</code> object
     */
    public int update(String statement, Setter setter) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            if (connection == null) {
                connection = source.getConnection();
            }
            pstmt = connection.prepareStatement(statement);
            if (setter != null) {
                setter.set(new PreparedStatementWrap(pstmt)); // set parameters
            }
            return pstmt.executeUpdate();
        } finally {
            close(pstmt);
        }
    }

    /**
     * @param statement a SQL query that returns resultset
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements or (2) 0 for SQL statements that return nothing
     * @throws SQLException if a database access error occurs; this method is called on a closed  <code>PreparedStatement</code> or the SQL statement returns a <code>ResultSet</code> object
     */
    public <T> T queryobj(String statement, Setter setter, Getter<T> getter) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            if (connection == null) {
                connection = source.getConnection();
            }
            pstmt = connection.prepareStatement(statement, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            pstmt.setFetchSize(1);
            if (setter != null) {
                setter.set(new PreparedStatementWrap(pstmt)); // set parameters
            }
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return getter.get(new ResultSetWrap(rs)); // retrieve values
            }
            return null;
        } finally {
            close(rs);
            close(pstmt);
        }
    }

    public void query(String statement, Setter setter, Doer doer) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            if (connection == null) {
                connection = source.getConnection();
            }
            pstmt = connection.prepareStatement(statement, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            if (setter != null) {
                setter.set(new PreparedStatementWrap(pstmt));
            }
            rs = pstmt.executeQuery();
            if (rs.next()) {
                doer.do_(new ResultSetWrap(rs));
            }
        } finally {
            close(rs);
            close(pstmt);
        }
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
            }
        }
    }

}
