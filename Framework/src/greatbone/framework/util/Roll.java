package greatbone.framework.util;

/**
 * A dictionary structure where elements are sorted in putting order.
 *
 * @param <K>
 */
public class Roll<K, V> {

    int[] buckets;
    Entry<K, V>[] entries;
    int count;

    public Roll(int capacity) {
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

    public V get(int index) {
        return entries[index].value;
    }

    public V last() {
        int index = count - 1;
        if (index >= 0) {
            return entries[count - 1].value;
        }
        return null;
    }

    public V get(K key) {
        int code = key.hashCode() & 0x7fffffff;
        for (int i = buckets[code % buckets.length]; i >= 0; i = entries[i].next) {
            Entry<K, V> e = entries[i];
            if (e.match(code, key)) return e.value;
        }
        return null;
    }

    public void put(K key, V value) {
        put(key, value, false);
    }

    @SuppressWarnings("unchecked")
    void put(K key, V value, boolean rehash) {
        // ensure double-than-needed capacity
        if (!rehash && count >= entries.length / 2) {
            Entry[] old = entries;
            int oldcount = count;
            reinitialize(entries.length * 2);
            // re-add old elements
            for (int i = 0; i < oldcount; i++) {
                Entry<K, V> e = old[i];
                put(e.key, e.value, true);
            }
        }

        int code = key.hashCode() & 0x7fffffff;
        int bucket = code % buckets.length; // target bucket
        int idx = buckets[bucket];
        while (idx != -1) {
            Entry<K, V> e = entries[idx];
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

    static class Entry<K, V> {

        int code; // lower 31 bits of hash code, -1 if unused

        int next; // index of next entry, -1 if last

        K key;

        V value;

        Entry(int code, int next, K key, V value) {
            this.code = code;
            this.next = next;
            this.key = key;
            this.value = value;
        }

        final K key() {
            return key;
        }

        final V value() {
            return value;
        }

        boolean match(int hash, K key) {
            return this.code == hash && this.key.equals(key);
        }

        public final String toString() {
            return key + "->" + value;
        }

    }


    public static void main(String[] args) {

        Roll<String, Integer> roll = new Roll<>(4);
        roll.put("michael", 1);
        roll.put("sam", 2);
        roll.put("yen", 3);
        roll.put("sledge", 4);

        roll.put("phil", 5);
        roll.put("john", 6);
    }

}