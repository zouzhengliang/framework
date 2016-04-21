package greatbone.framework.grid;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 */
public class BOOLEAN extends GridColumn<Boolean> {

    @Override
    public int size() {
        return 1;
    }

    @Override
    void load(GridData dat, ResultSet rs) throws SQLException {

    }

}
