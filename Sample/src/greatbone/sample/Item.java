package greatbone.sample;

import greatbone.framework.Decimal;
import greatbone.framework.grid.*;

/**
 * A service item, including a dish, an accessory, or some other stuff.
 */
public class Item extends GridData<Item> {

    //
    // COLUMNS

    static final STRING NAME = new STRING(12);

    static final STRING CATEGORY = new STRING(16);

    static final DECIMAL PRICE = new DECIMAL(2);

    static final BINARY ICON = new BINARY((byte) 32);

    //
    // ACCESSORS

    public String getName() {
        return NAME.get(this);
    }

    public void setName(String v) {
        NAME.put(this, v);
    }

    public String getCategory() {
        return CATEGORY.get(this);
    }

    public void setCategory(String v) {
        CATEGORY.put(this, v);
    }

    public Decimal getPrice() {
        return PRICE.get(this);
    }

    public void setPrice(Decimal v) {
        PRICE.put(this, v);
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
