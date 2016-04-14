package greatbone.sample;

import greatbone.framework.grid.CachePolicy;
import greatbone.framework.grid.GridPartedDataSet;
import greatbone.framework.grid.GridUtility;

/**
 */
@CachePolicy
public class ITEMS extends GridPartedDataSet<Item> {

    public ITEMS(GridUtility grid) {
        super(grid);
    }

    @Override
    protected void load(String arg) {
    }

    protected Class<Item> getDataClass() {
        return Item.class;
    }

}
