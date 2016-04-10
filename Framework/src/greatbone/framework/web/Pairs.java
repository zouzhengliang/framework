package greatbone.framework.web;

import io.undertow.connector.PooledByteBuffer;
import org.xnio.channels.StreamSourceChannel;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * A request content object model that can parse from a text content submission.
 */
public class Pairs {

    char[] content;

    int[] buckets;
    Entry[] entries;
    int count;

    public Pairs(int capacity) {
        // enxure power of 2
        int cap = 1;
        while (cap < capacity) {
            cap <<= 1;
        }
        reinitialize(cap);
    }

    @SuppressWarnings("unchecked")
    void reinitialize(int cap) { // alose used during rehash
        buckets = new int[cap];
        for (int i = 0; i < cap; i++) {
            buckets[i] = -1; // initialize all buckets to -1
        }
        entries = new Entry[cap];
        count = 0;
    }

    public int size() {
        return count;
    }

    public String getFirst(String key) {
        int code = key.hashCode() & 0x7fffffff;
        for (int i = buckets[code % buckets.length]; i >= 0; i = entries[i].next) {
            Entry e = entries[i];
            if (e.match(code, key)) {
                return e.first();
            }
        }
        return null;
    }

    public void put(String key, String value) {
        put(key, value, false);
    }

    @SuppressWarnings("unchecked")
    void put(String key, String value, boolean rehash) {
        // ensure double-than-needed capacity
        if (!rehash && count >= entries.length / 2) {
            Entry[] old = entries;
            int oldcount = count;
            reinitialize(entries.length * 2);
            // re-add old elements
            for (int i = 0; i < oldcount; i++) {
                Entry e = old[i];
//                put(e.key, e.value, true);
            }
        }

        int code = key.hashCode() & 0x7fffffff;
        int bucket = code % buckets.length; // target bucket
        int idx = buckets[bucket];
        while (idx != -1) {
            Entry e = entries[idx];
            if (e.match(code, key)) { // if exist then update the value
                e.value = value;
                return;
            }
            idx = entries[idx].next;  // skip to next index
        }
        // add a new entry
        idx = count;
        entries[idx] = new Entry(code, buckets[bucket], key, value);
        buckets[bucket] = idx;
        count++;
    }

    private void doParse(WebContext wc, final StreamSourceChannel channel) throws IOException {
        //0= parsing name
        //1=parsing name, decode required
        //2=parsing value
        //3=parsing value, decode required
        //4=finished
        int state = 0;

        String name;
        StringBuilder builder = new StringBuilder();
        int c = 0;
        try (PooledByteBuffer pooled = wc.exchange.getConnection().getByteBufferPool().allocate()) {
            final ByteBuffer buffer = pooled.getBuffer();
            do {
                buffer.clear();
                c = channel.read(buffer);
                if (c > 0) {
                    buffer.flip();
                    while (buffer.hasRemaining()) {
                        byte n = buffer.get();
                        switch (state) {
                            case 0: {
                                if (n == '=') {
                                    name = builder.toString();
                                    builder.setLength(0);
                                    state = 2;
                                } else if (n == '&') {
//                                    data.add(builder.toString(), "");
                                    builder.setLength(0);
                                    state = 0;
                                } else if (n == '%' || n == '+') {
                                    state = 1;
                                    builder.append((char) n);
                                } else {
                                    builder.append((char) n);
                                }
                                break;
                            }
                            case 1: {
                                if (n == '=') {
//                                    name = URLDecoder.decode(builder.toString(), charset);
                                    builder.setLength(0);
                                    state = 2;
                                } else if (n == '&') {
//                                    data.add(URLDecoder.decode(builder.toString(), charset), "");
                                    builder.setLength(0);
                                    state = 0;
                                } else {
                                    builder.append((char) n);
                                }
                                break;
                            }
                            case 2: {
                                if (n == '&') {
//                                    data.add(name, builder.toString());
                                    builder.setLength(0);
                                    state = 0;
                                } else if (n == '%' || n == '+') {
                                    state = 3;
                                    builder.append((char) n);
                                } else {
                                    builder.append((char) n);
                                }
                                break;
                            }
                            case 3: {
                                if (n == '&') {
//                                    data.add(name, URLDecoder.decode(builder.toString(), charset));
                                    builder.setLength(0);
                                    state = 0;
                                } else {
                                    builder.append((char) n);
                                }
                                break;
                            }
                        }
                    }
                }
            } while (c > 0);
            if (c == -1) {
                if (state == 2) {
//                    data.add(name, builder.toString());
                } else if (state == 3) {
//                    data.add(name, URLDecoder.decode(builder.toString(), charset));
                } else if (builder.length() > 0) {
                    if (state == 1) {
//                        data.add(URLDecoder.decode(builder.toString(), charset), "");
                    } else {
//                        data.add(builder.toString(), "");
                    }
                }
                state = 4;
//                exchange.putAttachment(FORM_DATA, data);
            }
        }
    }

    static class Entry {

        final int code; // lower 31 bits of hash code, -1 if unused

        int next; // index of next entry, -1 if last


        int keystart, keylen;

        int valuestart, valuelen;

        final String key;

        // either a string or a string array
        Object value;
        int size; // number of string elements

        Entry(int code, int next, String key, String value) {
            this(code, next, key, value, 1);
        }

        Entry(int code, int next, String key, Object value, int size) {
            this.code = code;
            this.next = next;
            this.key = key;
            this.value = value;
            this.size = size;
        }

        final String key() {
            return key;
        }

        final Object value() {
            return value;
        }

        boolean match(int code, String key) {
            return this.code == code && this.key.equals(key);
        }

        String first() {
            if (value instanceof String) {
                return (String) value;
            } else {
                return ((String[]) value)[0];
            }
        }

        public void append(String v) {
            String[] array;
            if (value instanceof String) {
                String old = (String) value;
                value = array = new String[8];
                array[0] = old;
            } else {
                array = (String[]) value;
            }
            array[size++] = v;

        }

        public final String toString() {
            return key + "->" + value;
        }

    }


}