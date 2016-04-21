package greatbone.framework.grid;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 */
public class LONG extends GridColumn {

    public int getValue(GridData dat) {
        return 0;
    }

    @Override
    public int size() {
        return 8;
    }

    @Override
    String dbtype() {
        return "BIGINT";
    }

    @Override
    void load(GridData dat, ResultSet rs) throws SQLException {

    }

    @Override
    void param(GridData dat, PreparedStatement pstmt) throws SQLException {

    }

}
