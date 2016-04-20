package greatbone.sample;

import greatbone.framework.grid.GridDataSet;
import greatbone.framework.grid.GridUtility;

/**
 * A replicated dataset that repsents any organizational unit, such as an agent, a shop, etc.
 */
public class ORGS extends GridDataSet<Org> {

    public ORGS(GridUtility grid) {
        super(grid, 12);
    }

}
