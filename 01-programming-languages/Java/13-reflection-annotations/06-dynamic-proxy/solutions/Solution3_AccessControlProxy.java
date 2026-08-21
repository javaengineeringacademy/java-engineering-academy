package academy.javaengineering.reflection.proxy.solutions;

import java.lang.annotation.*;
import java.lang.reflect.*;

public class Solution3_AccessControlProxy {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Secured {}

    public static <T> T createSecuredProxy(T target) {
        return (T) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            target.getClass().getInterfaces(),
            (proxy, method, args) -> {
                if (!method.isAnnotationPresent(Secured.class)) {
                    throw new SecurityException(
                        "Method " + method.getName() + " is not secured");
                }
                return method.invoke(target, args);
            }
        );
    }

    interface AdminService {
        @Secured String deleteAll();
        String getName();
    }
    static class AdminServiceImpl implements AdminService {
        public String deleteAll() { return "Deleted"; }
        public String getName() { return "Admin"; }
    }

    public static void main(String[] args) throws Exception {
        AdminService service = createSecuredProxy(new AdminServiceImpl());
        System.out.println(service.deleteAll());
        try { service.getName(); } catch (SecurityException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }
}
