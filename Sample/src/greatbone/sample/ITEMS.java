package greatbone.sample;

import greatbone.framework.grid.GridPartedDataSet;
import greatbone.framework.grid.GridUtility;
import greatbone.framework.grid.ReadPolicy;

/**
 */
@ReadPolicy()
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
