package middleware.resource.storage;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : ddv
 * @since : 2019/5/10 下午8:13
 */

public class Storage<K, V> {

    private static final Logger logger = LoggerFactory.getLogger(Storage.class);

    private Map<K, V> data = new HashMap<>();

    private Map<K, List<V>> dataList = new HashMap<>();

    public static <K, V> Storage valueOf(List<V> dataList) {
        Storage storage = new Storage();
        dataList.forEach(value -> {
            Field field = value.getClass().getDeclaredFields()[0];
            field.setAccessible(true);
            try {
                Object o = field.get(value);
                storage.data.put(o, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        });

        return storage;
    }

    public void addIntoStorageMap(K k, V v) {
        data.put(k, v);
    }

    public V getFromStorageMap(K k) {
        return data.get(k);
    }

    public Map<K, V> getStorageMap() {
        return data;
    }

    public Map<K, List<V>> getStorageListMap() {
        return dataList;
    }

    public List<V> getFromStorageList(K k) {
        return dataList.get(k);
    }

}
