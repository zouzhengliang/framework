package greatbone.sample;

import greatbone.framework.grid.GridData;
import greatbone.framework.grid.GridSchema;
import greatbone.framework.grid.MONEY;
import greatbone.framework.grid.STRING;
import greatbone.framework.web.Principal;

/**
 * A typed cursor for staffer
 */
public class Staffer extends GridData<Staffer> implements Principal {

    //
    // COLUMNS

    static final STRING LOGIN = new STRING(12);

    static final STRING CREDENTIAL = new STRING(12);

    static final STRING NAME = new STRING(12);

    static final MONEY WAGE = new MONEY();

    public int compareLogin(String v) {
        return LOGIN.compare(this, v);
    }

    public int compareCredential(String v) {
        return -1;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public String password() {
        return null;
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

    static final GridSchema<Staffer> SCHEMA = new GridSchema<>(Staffer.class,12);

}
