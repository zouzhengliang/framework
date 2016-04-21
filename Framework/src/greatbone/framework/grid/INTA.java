package greatbone.framework.grid;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * integer array
 */
public class INTA extends GridColumn<int[]> {

    int length;

    public INTA(int len) {

    }

    @Override
    public int size() {
        return length * 4;
    }

    @Override
    void load(GridData dat, ResultSet rs) throws SQLException {

    }

}
