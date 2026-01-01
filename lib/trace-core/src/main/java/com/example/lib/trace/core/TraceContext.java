package com.example.lib.trace.core;

/**
 * 현재 스레드의 트레이싱에 사용되는 상태를 보관하는 컨텍스트
 * ThreadLocal을 사용하여 동시성 이슈 없이 계층 정보를 유지
 */
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
