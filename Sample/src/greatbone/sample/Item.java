package greatbone.sample;

import greatbone.framework.grid.*;

/**
 * A service item, including a dish, an accessory, or some other stuff.
 */
public class Item extends GridData<Item> {

    //
    // COLUMNS

    static final STRING ID = new STRING(16); // 4 + 6 + 6

    static final STRING NAME = new STRING(12);

    static final MONEY PRICE = new MONEY();

    static final BINARY ICON = new BINARY((byte) 32);

    //
    // ACCESSORS

    public void id(String v) {
        ID.put(this, v);
    }

    public int name(String v) {
        return NAME.compare(this, v);
    }

    //
    // SCHEMA

    @Override
    protected GridSchema<Item> schema() {
        return SCHEMA;
    }

    static final GridSchema<Item> SCHEMA = new GridSchema<>(Item.class);


}
