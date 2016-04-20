package greatbone.sample;

import greatbone.framework.grid.*;
import greatbone.framework.web.Principal;

/**
 * A typed cursor for staffer
 */
public class Staffer extends GridData<Staffer> implements Principal {

    //
    // COLUMNS

    static final KEY ID = new KEY(12);

    static final STRING CREDENTIAL = new STRING(12);

    static final STRING NAME = new STRING(12);

    static final DECIMAL WAGE = new DECIMAL(2);

    @Override
    public String getName() {
        return NAME.getValue(this);
    }

    public String getCredential() {
        return CREDENTIAL.getValue(this);
    }

    @Override
    public int roles() {
        return 0;
    }

    //
    // SCHEMA

    @Override
    protected GridSchema<Staffer> schema() {
        return SCHEMA;
    }

    static final GridSchema<Staffer> SCHEMA = new GridSchema<>(Staffer.class);

}
