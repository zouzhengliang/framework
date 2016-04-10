package greatbone.sample;

import greatbone.framework.grid.*;

/**
 */
public class Event extends GridData<Event> {

    //
    // COLUMNS

    static final INT ID = new INT();

    static final STRING TEXT = new STRING(128);

    static final BINARY ICON = new BINARY((byte) 32);

    public int id() {
        return ID.get(this);
    }

    public void id(int id) {
        ID.put(this, id);
    }

    public String text() {
        return TEXT.get(this);
    }

    public void text(String v) {
        TEXT.put(this, v);
    }

    @Override
    protected GridSchema<Event> schema() {
        return SCHEMA;
    }

    static final GridSchema<Event> SCHEMA = new GridSchema<>(Event.class);

}
