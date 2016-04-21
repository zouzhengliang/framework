package greatbone.framework.grid;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A character string encoded in ASCII
 */
public class KEY extends GridColumn<String> {

    // maxinum number of characters
    final int length;

    public KEY(int length) {
        this.length = length;
    }

    public String getValue(GridData dat) {
        return dat.getAscii(offset, length);
    }

    public void putValue(GridData dat, String v) {
        dat.putAscii(offset, v, length);
    }

    public int tryValue(GridData dat, String v) {
        return -1;
    }

    @Override
    int size() {
        return length;
    }

    @Override
    void load(GridData dat, ResultSet rs) throws SQLException {
        putValue(dat, rs.getString(ordinal));
    }

    @Override
    void param(GridData dat, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(ordinal, getValue(dat));
    }

}
