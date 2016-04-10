package greatbone.framework.grid;

/**
 * The structural information about a data column that is declared in a data entry class..
 */
abstract class GridColumn<T> {

    String name;

    int origin;

    int offset;

    public final String name() {
        return name;
    }

    void init(String name, int offset) {
        this.name = name;
        this.offset = offset;
    }

    public int size() {
        return 0;
    }

}
