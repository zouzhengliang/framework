package greatbone.sample;

import greatbone.framework.grid.GridDataSet;
import greatbone.framework.grid.GridUtility;

/**
 *
 */
public class USERS extends GridDataSet<User> {

    public USERS(GridUtility grid) {
        super(grid,12);
    }

    @Override
    protected void load(String arg) {


    }

}
