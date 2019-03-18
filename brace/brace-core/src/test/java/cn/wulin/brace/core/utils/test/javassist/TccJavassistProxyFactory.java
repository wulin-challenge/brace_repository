package cn.wulin.brace.core.utils.test.javassist;

/**
 * Created by changming.xie on 1/14/17.
 */
public class TccJavassistProxyFactory {

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<?>[] interfaces) {
        return (T) TccProxy.getProxy(interfaces);
    }
}
