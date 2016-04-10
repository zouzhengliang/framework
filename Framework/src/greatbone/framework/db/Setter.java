package greatbone.framework.db;

import java.sql.SQLException;

/**
 */
public interface Setter {

    void set(PreparedStatementWrap wrap) throws SQLException;

}
