package greatbone.sample;

import greatbone.framework.grid.CachePolicy;
import greatbone.framework.grid.GridDataSet;
import greatbone.framework.grid.GridUtility;

/**
 */
@CachePolicy
public class EVENTS extends GridDataSet<Event> {

    public EVENTS(GridUtility grid) {
        super(grid, 12);
    }

    protected void load(String arg) {
    }

    protected Class<Event> getDataClass() {
        return Event.class;
    }

}
