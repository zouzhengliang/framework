package greatbone.framework.grid;

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
    void load(GridData dat, ResultSet rs) throws SQLException {

    }

}
