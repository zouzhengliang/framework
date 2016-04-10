package greatbone.sample;

import greatbone.framework.grid.*;

/**
 */
public class Order extends GridData<Order> {

    //
    // COLUMNS

    static final INT ID = new INT();

    static final TIMESTAMP DATE = new TIMESTAMP();

    static final STRING NAME = new STRING(12);

    static final INTA OPS = new INTA(12);

    static final DETAILLIST DETAILS = new DETAILLIST(12);

    public int id() {
        return ID.get(this);
    }

    public void id(int v) {
        ID.put(this, v);
    }

    public String name() {
        return NAME.get(this);
    }

    public void name(String v) {
        NAME.put(this, v);
    }

    public void details_item(int index, int v) {
        DETAILS.ID.put(this, v);
    }

    @Override
    protected GridSchema<Order> schema() {
        return SCHEMA;
    }

    static final GridSchema<Order> SCHEMA = new GridSchema<>(Order.class);

}

/**
 */
class DETAILLIST extends LIST {

    final INT ID = new INT();

    final STRING NAME = new STRING(12);

    public DETAILLIST(int maxlen) {
        super(maxlen);
    }

}


