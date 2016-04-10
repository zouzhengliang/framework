package greatbone.sample;

import greatbone.framework.grid.GridPartedDataSet;
import greatbone.framework.grid.GridUtility;

/**
 */
public class ORGS extends GridPartedDataSet<Org> {

    public ORGS(GridUtility grid) {
        super(grid);
    }

    @Override
    protected void load(String arg) {
    }

}
