package greatbone.framework.grid;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 */
public class TIMESTAMP extends GridColumn<Timestamp> {

    @Override
    public int size() {
        return 8;
    }

    @Override
    void load(GridData dat, ResultSet rs) throws SQLException {

    }

}
