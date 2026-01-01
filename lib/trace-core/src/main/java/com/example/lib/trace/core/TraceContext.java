package com.example.lib.trace.core;

public class TraceContext {

    private static final ThreadLocal<Integer> depthHolder = ThreadLocal.withInitial(() -> 0);

    public static int incrementDepth() {
        int depth = depthHolder.get();
        depthHolder.set(depth + 1);
        return depth;
    }

    public static void decrementDepth() {
        int depth = depthHolder.get();
        if (depth > 0) {
            depthHolder.set(depth - 1);
        }
    }

    public static int getDepth() {
        return depthHolder.get();
    }

    public static void setDepth(int depth) {
        depthHolder.set(depth);
    }

    public static void clear() {
        depthHolder.remove();
    }
}
