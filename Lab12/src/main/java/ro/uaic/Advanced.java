package ro.uaic;

import javassist.*;
import javax.tools.*;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Advanced {
    public static void main(String[] args) throws Exception {
        String sourceFolderPath = args[0];
        File folder = new File(sourceFolderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Invalid directory path: " + folder.getAbsolutePath());
            return;
        }

        compileJavaFiles(folder);

        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(folder.getPath());

        URL[] urls = { folder.toURI().toURL() };
        URLClassLoader loader = new URLClassLoader(urls);
        List<Class<? extends Annotation>> customAnnotations = new ArrayList<>();

        for (File file : folder.listFiles((dir, name) -> name.endsWith(".class"))) {
            String className = file.getName().replace(".class", "");
            CtClass ct = pool.get(className);

            if (ct.isAnnotation()) {
                Class<?> clazz = ct.toClass();
                customAnnotations.add(clazz.asSubclass(Annotation.class));
            }
        }
        loader.close();

        for (File file : folder.listFiles((dir, name) -> name.endsWith(".class"))) {
            String className = file.getName().replace(".class", "");
            CtClass ctClass = pool.get(className);

            if (ctClass.isAnnotation() || ctClass.isInterface()) {
                ctClass.detach();
                continue;
            }

            for (CtMethod method : ctClass.getDeclaredMethods()) {
                method.insertBefore(
                        "{ System.out.println(\"[LOG] Executing method: " + method.getName() + "\"); }");
            }

            Class<?> modifiedClass = ctClass.toClass();

            invokeAnnotatedMethods(modifiedClass, customAnnotations);

            ctClass.detach();
        }
    }

    private static void compileJavaFiles(File folder) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".java"));

        if (files != null && files.length > 0) {
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
            Iterable<? extends JavaFileObject> compilationUnits = fileManager
                    .getJavaFileObjectsFromFiles(Arrays.asList(files));

            compiler.getTask(null, fileManager, null, null, null, compilationUnits).call();
            System.out.println("Compilation complete.");
        }
    }

    private static void invokeAnnotatedMethods(Class<?> clazz, List<Class<? extends Annotation>> annotations)
            throws Exception {
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
                System.out.println("Invoked: " + m.getName() + " (no-args)");
                m.invoke(obj);
            } else if (params.length == 1 && (params[0] == int.class || params[0] == Integer.class)) {
                int mockValue = (int) (Math.random() * 100);
                System.out.println("Invoked: " + m.getName() + " (with param: " + mockValue + ")");
                m.invoke(obj, mockValue);
            }
        } catch (Exception e) {
            System.err.println("Failed to invoke " + m.getName() + ": " + e.getCause());
        }
    }
}
