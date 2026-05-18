package ro.uaic;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Homework
 */
public class Homework {

    public static void main(String[] args) throws Exception {
        if (args.length == 0)
            return;

        File path = new File(args[0]);
        if (!path.isDirectory())
            return;

        URL[] urls = { path.toURI().toURL() };
        URLClassLoader loader = new URLClassLoader(urls);

        List<Class<?>> allFileClasses = new ArrayList<>();
        List<Class<? extends Annotation>> customAnnotations = new ArrayList<>();

        for (File file : path.listFiles((dir, name) -> name.endsWith(".class"))) {
            String className = file.getName().replace(".class", "");
            Class<?> clazz = loader.loadClass(className);
            allFileClasses.add(clazz);

            if (clazz.isAnnotation()) {
                customAnnotations.add(clazz.asSubclass(Annotation.class));
            }
        }

        for (Class<?> clazz : allFileClasses) {
            if (Modifier.isPublic(clazz.getModifiers()) && !clazz.isAnnotation()) {
                printPrototype(clazz);
                invokeAnnotatedMethods(clazz, customAnnotations);
            }
        }
        loader.close();
    }

    private static void printPrototype(Class<?> clazz) {
        System.out.println("\n--- Class: " + clazz.getSimpleName() + " ---");
        for (Method m : clazz.getDeclaredMethods()) {
            String mod = Modifier.toString(m.getModifiers());
            System.out.println(mod + " " + m.getReturnType().getSimpleName() + " " + m.getName() + "(...)");
        }
    }

    private static void invokeAnnotatedMethods(Class<?> clazz, List<Class<? extends Annotation>> annotations) {
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            for (Method method : clazz.getDeclaredMethods()) {
                for (Class<? extends Annotation> ann : annotations) {
                    if (method.isAnnotationPresent(ann)) {
                        execute(method, instance);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Could not instantiate " + clazz.getName());
        }
    }

    private static void execute(Method m, Object obj) {
        try {
            m.setAccessible(true);
            Class<?>[] params = m.getParameterTypes();

            if (params.length == 0) {
                m.invoke(obj);
                System.out.println("Invoked: " + m.getName() + " (no-args)");
            } else if (params.length == 1 && (params[0] == int.class || params[0] == Integer.class)) {
                int mockValue = (int) (Math.random() * 100);
                m.invoke(obj, mockValue);
                System.out.println("Invoked: " + m.getName() + " (with param: " + mockValue + ")");
            }
        } catch (Exception e) {
            System.err.println("Failed to invoke " + m.getName() + ": " + e.getCause());
        }
    }
}
