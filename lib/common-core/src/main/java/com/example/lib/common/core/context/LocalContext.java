package com.example.lib.common.core.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

    public static int incrementDepth() {
        int depth = get("trace_depth", 0);
        put("trace_depth", depth + 1);
        return depth;
    }

    public static void decrementDepth() {
        int depth = get("trace_depth", 0);
        if (depth > 0) {
            put("trace_depth", depth - 1);
        }
    }

    public static int getDepth() {
        return get("trace_depth", 0);
    }

    @SuppressWarnings("unchecked")
    private static <T> T get(String key, T defaultValue) {
        Object value = storage.get().get(key);
        return value != null ? (T) value : defaultValue;
    }
}
