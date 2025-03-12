import java.lang.reflect.Parameter;

public class Test {
    public void exampleMethod(String name, int age) {}

    public static void main(String[] args) throws NoSuchMethodException {
        Parameter[] params = Test.class.getMethod("exampleMethod", String.class, int.class).getParameters();
        for (Parameter param : params) {
            System.out.println("Parameter name: " + param.getName());
        }
    }
}
