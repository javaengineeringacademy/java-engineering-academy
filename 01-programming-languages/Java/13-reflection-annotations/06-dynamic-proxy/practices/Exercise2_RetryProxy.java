package academy.javaengineering.reflection.proxy.practices;

import java.lang.reflect.*;

public class Exercise2_RetryProxy {

    public static <T> T createRetryProxy(T target, int maxRetries) {
        return (T) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            target.getClass().getInterfaces(),
            (proxy, method, args) -> {
                Exception lastException = null;
                for (int i = 0; i <= maxRetries; i++) {
                    try {
                        return method.invoke(target, args);
                    } catch (Exception e) {
                        lastException = e;
                        if (e instanceof InvocationTargetException) {
                            lastException = (Exception) ((InvocationTargetException) e).getTargetException();
                        }
                    }
                }
                throw lastException;
            }
        );
    }

    interface FlakyService { String doWork(); }
    static class FlakyServiceImpl implements FlakyService {
        private int attempts = 0;
        public String doWork() {
            if (++attempts < 3) throw new RuntimeException("Failure " + attempts);
            return "Success";
        }
    }

    public static void main(String[] args) throws Exception {
        FlakyService service = createRetryProxy(new FlakyServiceImpl(), 3);
        System.out.println(service.doWork());
    }
}
