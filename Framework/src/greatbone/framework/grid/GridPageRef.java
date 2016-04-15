package greatbone.framework.grid;

import org.xnio.StreamConnection;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * The proxy of a remote parge.
 */
class GridPageRef<D extends GridData<D>> extends GridPage<D> {

    // cache
    GridClient client;

    GridPageRef(GridDataSet<D> parent, String id, GridClient client) {
        super(parent, id);

        this.client = client;
    }

    @Override
    public D get(String key) {
        StreamConnection conn = null;
        try (GridContext gc = new GridContext(conn = client.checkout())) {
            //
            // parent.key
            // id;
            // key
            client.call(gc);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.checkin(conn);
        }
        return null;
    }

    @Override
    public D put(String key, D dat) {
        StreamConnection conn = null;
        try (GridContext gc = new GridContext(conn = client.checkout())) {
            //
            // parent.key
            // id;
            // key
            client.call(gc);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.checkin(conn);
        }
        return null;
    }

    @Override
    public D search(Critera<D> filter) {
        StreamConnection conn = null;
        try (GridContext gc = new GridContext(conn = client.checkout())) {
            //
            // parent.key
            // id;
            // key

            // serialize the critera object or lambda expression as call body
            ObjectOutputStream out = new ObjectOutputStream(gc.ostream);
            out.writeObject(filter);
            out.close();

            client.call(gc);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.checkin(conn);
        }
        return null;
    }

}
