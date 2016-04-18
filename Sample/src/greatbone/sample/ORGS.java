package greatbone.sample;

import greatbone.framework.grid.GridDataSet;
import greatbone.framework.grid.GridUtility;

/**
 */
public class ORGS extends GridDataSet<Org> {

    public ORGS(GridUtility grid) {
        super(grid, 12);
    }

    @Override
    protected void load(String arg) {
    }

}
