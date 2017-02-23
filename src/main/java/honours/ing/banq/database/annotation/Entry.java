package honours.ing.banq.database.annotation;

import java.util.Map;

/**
 * Created by Kevin Witlox on 23-2-2017.
 */
public class Entry<K, V> implements Map.Entry<K, V> {

    private K key;
    private V value;

    public Entry() {
        this.key = null;
        this.value = null;
    }

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }

}
