package greatbone.sample;

import greatbone.framework.grid.GridDataSet;
import greatbone.framework.grid.GridUtility;

/**
 * matches key to a particular data partition (shard)
 */
public class ORDERS extends GridDataSet<Order> {


    public ORDERS(GridUtility grid) {
        super(grid, 12);
    }

    @Override
    protected void load(String arg) {

        // SELECT
    }

    public void partitionLogic() {

    }



}
