package greatbone.framework.grid;

/**
 * A column that contains an array of structs.
 */
class GridStructsColumn extends GridColumn {

    final STRUCT[] structs;

    int size;

    GridStructsColumn(STRUCT[] structs) {
        this.structs = structs;
    }

    @Override
    int size() {
        return 0;
    }

}
