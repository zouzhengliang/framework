package greatbone.sample;

import greatbone.framework.grid.GridData;
import greatbone.framework.grid.GridSchema;
import greatbone.framework.grid.STRING;
import greatbone.framework.web.Principal;

/**
 * A directory principal, either a management staff, a user or a shop.
 */
public class User extends GridData<User> implements Principal {

    //
    // COLUMNS

    static final STRING LOGIN = new STRING(12);

    static final STRING CREDENTIAL = new STRING(12);

    static final STRING NAME = new STRING(12);

    //
    // ACCESSORS

    public int getID() {
        return 0;
    }

    public int compareLogin(String v) {
        return LOGIN.tryValue(this, v);
    }

    public int compareCredential(String v) {
        return -1;
    }


    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getCredential() {
        return null;
    }

    @Override
    public int roles() {
        return 0;
    }

    //
    // SCHEMA

    @Override
    protected GridSchema<User> schema() {
        return SCHEMA;
    }

    static final GridSchema<User> SCHEMA = new GridSchema<>(User.class);

}
