package li.chee.supermachine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.junit.Test;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertArrayEquals;

public class ScannerTest {
    @Test
    public void streamScanner() throws Exception {

        String[] result =
                StreamScanner.scan(Stream.concat(IntStream.range(0, 40).boxed(), DoubleStream.of(24.0).boxed()))
                        .find(Integer.class)
                        .filter(i -> i.intValue() % 4 == 0)
                        .extract(Object::toString)
                        .then((s -> s.filter(x -> x.startsWith("2"))),
                                (s -> s.filter(x -> x.endsWith("2"))))
                        .stream()
                        .toArray(String[]::new);

        assertArrayEquals(new String[]{"12", "20", "24", "28", "32"}, result);

    }

    @Test
    public void binaryTree() {
        BinaryTree tree = new BinaryTree("root",
                new BinaryTree("left",
                        new BinaryTree("left-left", null, null, null),
                        new BinaryTree("left-right", null, null, null), null),
                new BinaryTree("right",
                        new BinaryTree("right-left", null, null, null),
                        new BinaryTree("right-right", null, null, null), null), null);
        String[] result =
                BeanScanner.from(tree).find(String.class).stream().toArray(String[]::new);

        assertArrayEquals(new String[]{"root", "left", "right",
                        "left-left", "left-right",
                        "right-left", "right-right"},
                result);
    }

    @Test
    public void addresses() {

    }

    @Data
    @Builder
    public static class BinaryTree {
        private String name;
        private BinaryTree left;
        private BinaryTree right;
        private Object element;
    }

    @Data
    @Builder
    public static class Person {
        private String name;
        private Address[] addresses;
    }

    @Data
    @Builder
    public static class Address {
        private String street;
        private City city;
    }

    @Data
    @Builder
    public static class City {
        Person mayor;
    }
}