package greatbone.sample;

import greatbone.framework.grid.GridReplicatedDataSet;
import greatbone.framework.grid.GridUtility;

/**
 */
public class STAFFERS extends GridReplicatedDataSet<String, Staffer> {


    public STAFFERS(GridUtility grid) {
        super(grid);
    }

    @Override
    protected void load(String arg) {
    }

    protected Class<Staffer> getDataClass() {
        return Staffer.class;
    }

}
