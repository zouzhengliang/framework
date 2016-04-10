package greatbone.sample;

import greatbone.framework.grid.GridPartedDataSet;
import greatbone.framework.grid.GridUtility;

/**
 * matches key to a particular data partition (shard)
 */
public class ORDERS extends GridPartedDataSet<Order> {


    public ORDERS(GridUtility grid) {
        super(grid);
    }

    @Override
    protected void load(String arg) {

        // SELECT
    }

    public void partitionLogic() {

    }

}
