/**
 * Test3
 */
public class Test3 {
    @MyTest
    public void sayHello() {
        System.out.println("Hello!.");
    }

    @MyTest
    public void calculate(int x) {
        System.out.println("The value received was: " + x);
    }

    public void ignoreMe() {
        System.out.println("This should not run (no annotation).");
    }
}
