package greatbone.framework.grid;

import org.xnio.StreamConnection;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * A proxy that points to its remote origin page.
 */
class GridPageRef<D extends GridData<D>> extends GridPage<D> {

    // client endpoint to the origin peer
    GridClient origincli;

    // status of origin
    int orginstatus;

    // client endpoint to the backup peer
    volatile GridClient backupcli;

    int backupstatus;

    GridPageRef(GridDataSet<D> parent, String id, GridClient origincli) {
        super(parent, id);

        this.origincli = origincli;
    }

    @Override
    public D get(String key) {
        StreamConnection conn = null;
        try (GridContext gc = new GridContext(conn = this.origincli.checkout())) {
            //
            // parent.key
            // id;
            // key
            this.origincli.call(gc);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.origincli.checkin(conn);
        }
        return null;
    }

    @Override
    public D put(String key, D dat) {
        StreamConnection conn = null;
        try (GridContext gc = new GridContext(conn = this.origincli.checkout())) {
            //
            // parent.key
            // id;
            // key
            this.origincli.call(gc);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.origincli.checkin(conn);
        }
        return null;
    }

    @Override
    public D search(Critera<D> filter) {
        StreamConnection conn = null;
        try (GridContext gc = new GridContext(conn = this.origincli.checkout())) {
            //
            // parent.key
            // id;
            // key

            // serialize the critera object or lambda expression as call body
            ObjectOutputStream out = new ObjectOutputStream(gc.ostream);
            out.writeObject(filter);
            out.close();

            this.origincli.call(gc);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.origincli.checkin(conn);
        }
        return null;
    }

}
