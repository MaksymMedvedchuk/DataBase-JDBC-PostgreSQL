import java.util.HashSet;

public class Test {

    public static void main(String[] args) {
        HashSet<String> strings = new HashSet<>();
        strings.add("Hello");
        strings.add("Dy");
        strings.add("Hi");
        strings.add("Hello");

        System.out.println(strings.size());
        }

     }

