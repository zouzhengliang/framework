package greatbone.framework.grid;

/**
 * The structural information about a data column that is declared in a data entry class..
 */
abstract class GridColumn<T> {

    // lowercase name
    String name;

    // quoted name, used in database
    String qname;

    int origin;

    int offset;

    final String name() {
        return name;
    }

    final String qname() {
        return qname;
    }

    void init(String name, int offset) {
        this.name = name.toLowerCase();
        this.qname = "\"" + name + "\"";
        this.offset = offset;
    }

    abstract int size();

    final int tail() {
        return offset + size();
    }

}
