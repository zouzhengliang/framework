package greatbone.sample;

import greatbone.framework.grid.GridData;
import greatbone.framework.grid.GridSchema;
import greatbone.framework.grid.INT;
import greatbone.framework.grid.STRING;

/**
 * A call for service, initiated by a guest or the system.
 */
public class Call extends GridData<Call> {

    //
    // COLUMNS

    static final INT ID = new INT();

    static final STRING TEXT = new STRING(12);

    //
    // SCHEMA

    @Override
    protected GridSchema<Call> schema() {
        return SCHEMA;
    }

    static final GridSchema<Call> SCHEMA = new GridSchema<>(Call.class, 12);

}
