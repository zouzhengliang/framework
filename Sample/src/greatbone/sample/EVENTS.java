package greatbone.sample;

import greatbone.framework.grid.CachePolicy;
import greatbone.framework.grid.GridReplicatedDataSet;
import greatbone.framework.grid.GridUtility;

/**
 */
@CachePolicy
public class EVENTS extends GridReplicatedDataSet<Event> {

    public EVENTS(GridUtility grid) {
        super(grid);
    }

    protected void load(String arg) {
    }

    protected Class<Event> getDataClass() {
        return Event.class;
    }

}
