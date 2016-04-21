package greatbone.sample;

import greatbone.framework.Decimal;
import greatbone.framework.grid.*;

/**
 * A service item, including a dish, an accessory, or some other stuff.
 */
public class Item extends GridData<Item> {

    //
    // COLUMNS

    static final KEY ID = new KEY(12);

    static final STRING NAME = new STRING(12);

    static final STRING CATEGORY = new STRING(16);

    static final DECIMAL PRICE = new DECIMAL(2);

    static final BINARY ICON = new BINARY((byte) 32);

    //
    // ACCESSORS

    public String getName() {
        return NAME.getValue(this);
    }

    public void setName(String v) {
        NAME.putValue(this, v);
    }

    public String getCategory() {
        return CATEGORY.getValue(this);
    }

    public void setCategory(String v) {
        CATEGORY.putValue(this, v);
    }

    public Decimal getPrice() {
        return PRICE.getValue(this);
    }

    public void setPrice(Decimal v) {
        PRICE.putValue(this, v);
    }

    public byte[] getIcon() {
        return ICON.get(this);
    }

    public void setIcon(byte[] v) {
        ICON.put(this, v);
    }

    //
    // SCHEMA

    @Override
    protected GridSchema<Item> schema() {
        return SCHEMA;
    }

    static final GridSchema<Item> SCHEMA = new GridSchema<>(Item.class);


}
