package ro.uaic;

import java.lang.reflect.Method;

/**
 * Compulsory
 */
public class Compulsory {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide a class name.");
            return;
        }

        String className = args[0];

        try {
            Class<?> clazz = Class.forName(className);

            Method runMethod = clazz.getMethod("run");

            Object instance = clazz.getDeclaredConstructor().newInstance();

            runMethod.invoke(instance);

        } catch (ClassNotFoundException e) {
            System.err.println("Error: Nu exista clasa: " + className);
        } catch (NoSuchMethodException e) {
            System.err.println("Error: Nu exista metoda publica run() in clasa: " + className);
        } catch (Exception e) {
            System.err.println("Error executie: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
