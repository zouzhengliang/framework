package greatbone.sample;

import greatbone.framework.grid.GridDataSet;
import greatbone.framework.grid.GridUtility;

/**
 * A replicated dataset that repsents a login managerial staffer.
 */
public class STAFFERS extends GridDataSet<Staffer> {

    public STAFFERS(GridUtility grid) {
        super(grid, 12);
    }

}
