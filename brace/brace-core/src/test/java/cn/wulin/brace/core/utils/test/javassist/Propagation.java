package cn.wulin.brace.core.utils.test.javassist;

/**
 * Created by changming.xie on 1/18/17.
 */
public enum Propagation {
    REQUIRED(0),
    SUPPORTS(1),
    MANDATORY(2),
    REQUIRES_NEW(3);

    private final int value;

    private Propagation(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}