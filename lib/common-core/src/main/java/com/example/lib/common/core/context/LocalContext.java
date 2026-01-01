package com.example.lib.common.core.context;

import java.util.HashMap;
import java.util.Map;

/**
 * ThreadLocal을 이용하여 로컬 컨텍스트를 관리하는 클래스
 */
public class LocalContext {

    private static final ThreadLocal<Map<String, Object>> storage = ThreadLocal.withInitial(HashMap::new);

    public static void put(String key, Object value) {
        if (value == null) {
            storage.get().remove(key);
        } else {
            storage.get().put(key, value);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        return (T) storage.get().get(key);
    }

    public static Map<String, Object> getAll() {
        return new HashMap<>(storage.get());
    }

    public static void clear() {
        storage.remove();
    }
}
