package greatbone.framework.util;

import java.io.IOException;

/**
 * A general object pool
 */
public abstract class Pool<T> {

    final T[] elements;

    int head;

    int tail;

    @SuppressWarnings("unchecked")
    protected Pool(int capacity) {
        elements = (T[]) new Object[capacity];
    }

    protected abstract T create() throws IOException;

    /**
     * To borrow an object from the pool, create one if there is none left.
     */
    public T checkout() throws IOException {
        synchronized (elements) {
            int head = this.head;
            T obj = elements[head];
            if (obj == null) {
                obj = create();
                elements[head] = null;
                this.head = (head + 1) & (elements.length - 1);
            }
            return obj;
        }
    }

    public void checkin(T obj) {
        synchronized (elements) {
        }
    }

}
