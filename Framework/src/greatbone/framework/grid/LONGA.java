package greatbone.framework.grid;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * integer array
 */
public class LONGA extends GridColumn<long[]> {

    int length;

    public LONGA(int len) {
    }

    @Override
    public int size() {
        return length * 8;
    }

    @Override
    void load(GridData dat, ResultSet rs) throws SQLException {

    }

}
