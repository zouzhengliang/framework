package greatbone.framework.grid;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * string array
 */
public class STRINGA extends GridColumn {

    int length;

    int num;

    public STRINGA(int len, int size) {

    }

    @Override
    public int size() {
        return num * length;
    }

    public int compare(GridData data, String v) {
        return -1;
    }

    @Override
    void load(GridData dat, ResultSet rs) throws SQLException {

    }

}
