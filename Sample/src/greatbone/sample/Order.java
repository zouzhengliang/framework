package greatbone.sample;

import greatbone.framework.grid.*;

/**
 */
public class Order extends GridData<Order> {

    //
    // COLUMNS

    static final KEY ID = new KEY(12);

    static final TIMESTAMP DATE = new TIMESTAMP();

    static final STRING NAME = new STRING(12);

    static final INTA OPS = new INTA(12);

    static final DETAILS DETAILS = new DETAILS(12);

    final DECIMAL TOTAL = new DECIMAL(2);


    public String getId() {
        return ID.get(this);
    }

    public void setId(String v) {
        ID.put(this, v);
    }

    public String getName() {
        return NAME.get(this);
    }

    public void setName(String v) {
        NAME.put(this, v);
    }

    public String getItemId(int index) {
        return DETAILS.ITEMID.get(this);
    }

    public void setItemId(int index, String v) {
        DETAILS.ITEMID.put(this, v);
    }

    //
    // SCHEMA

    @Override
    protected GridSchema<Order> schema() {
        return SCHEMA;
    }

    static final GridSchema<Order> SCHEMA = new GridSchema<>(Order.class);

}

/**
 */
class DETAILS extends ARRAY {

    final STRING ITEMID = new STRING(12);

    final STRING ITEMNAME = new STRING(12);

    final INT QTY = new INT();

    final DECIMAL PRICE = new DECIMAL(12);

    public DETAILS(int maxlen) {
        super(maxlen);
    }

}


