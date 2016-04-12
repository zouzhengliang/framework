package greatbone.sample;

import greatbone.framework.grid.GridReplicatedDataSet;
import greatbone.framework.grid.GridUtility;
import greatbone.framework.grid.ReadPolicy;

/**
 */
@ReadPolicy()
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
