package greatbone.framework.grid;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

/**
 */
public class TIME extends GridColumn<Time> {

    @Override
    int size() {
        return 0;
    }

    @Override
    String dbtype() {
        return "TIME";
    }

    @Override
    void load(GridData dat, ResultSet rs) throws SQLException {

    }

    @Override
    void param(GridData dat, PreparedStatement pstmt) throws SQLException {

    }

}
