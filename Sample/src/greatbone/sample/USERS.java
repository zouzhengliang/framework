package greatbone.sample;

import greatbone.framework.grid.GridPartedDataSet;
import greatbone.framework.grid.GridUtility;

/**
 *
 */
public class USERS extends GridPartedDataSet<User> {

    public USERS(GridUtility grid) {
        super(grid);
    }

    @Override
    protected void load(String arg) {


    }

}
