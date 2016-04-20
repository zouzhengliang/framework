package greatbone.sample;

import greatbone.framework.grid.*;

/**
 */
public class Event extends GridData<Event> {

    //
    // COLUMNS

    static final KEY ID = new KEY(8);

    static final STRING TEXT = new STRING(128);

    static final BINARY ICON = new BINARY((byte) 32);

    //
    // ACCESSORS

    public String getId() {
        return ID.get(this);
    }

    public void setId(String id) {
        ID.put(this, id);
    }

    public String getText() {
        return TEXT.get(this);
    }

    public void setText(String v) {
        TEXT.put(this, v);
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
    protected GridSchema<Event> schema() {
        return SCHEMA;
    }

    static final GridSchema<Event> SCHEMA = new GridSchema<>(Event.class);

}
