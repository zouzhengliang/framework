package greatbone.sample;

import greatbone.framework.Decimal;
import greatbone.framework.grid.*;

/**
 */
public class Order extends GridData<Order> {

    //
    // COLUMNS

    static final KEY ID = new KEY(12);

    static final TIMESTAMP DATE = new TIMESTAMP();

    static final STRING NAME = new STRING(12);

    static final INT OPS = new INT();

    static final DETAIL[] DETAILS = {new DETAIL(), new DETAIL(), new DETAIL(), new DETAIL(), new DETAIL(), new DETAIL(), new DETAIL(), new DETAIL()};

    static final DECIMAL TOTAL = new DECIMAL(2);


    public String getId() {
        return ID.getValue(this);
    }

    public void setId(String v) {
        ID.putValue(this, v);
    }

    public String getName() {
        return NAME.getValue(this);
    }

    public void setName(String v) {
        NAME.putValue(this, v);
    }

    public String getItemId(int index) {
        return DETAILS[index].ITEMID.getValue(this);
    }

    public void setItemId(int index, String v) {
        DETAILS[index].ITEMID.putValue(this, v);
    }

    public short getQty(int index) {
        return DETAILS[index].QTY.getValue(this);
    }

    public void setQty(int index, short v) {
        DETAILS[index].QTY.putValue(this, v);
    }

    public Decimal getPrice(int index) {
        return DETAILS[index].PRICE.getValue(this);
    }

    public void setPrice(int index, Decimal v) {
        DETAILS[index].PRICE.putValue(this, v);
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
class DETAIL extends STRUCT {

    final KEY ITEMID = new KEY(12);

    final STRING ITEMNAME = new STRING(12);

    final SHORT QTY = new SHORT();

    final DECIMAL PRICE = new DECIMAL(2);

}


