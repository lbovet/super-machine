package li.chee.supermachine;

import lombok.Builder;
import lombok.Data;
import org.junit.Test;

import java.util.*;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static li.chee.supermachine.BeanScanner.from;
import static org.junit.Assert.assertArrayEquals;

public class ScannerTest {
    @Test
    public void streamScanner() throws Exception {
        String[] result =
                StreamScanner.scan(Stream.concat(IntStream.range(0, 40).boxed(), DoubleStream.of(24.0).boxed()))
                        .find(Integer.class)
                        .filter(i -> i % 4 == 0)
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
                from(tree).find(String.class).stream().toArray(String[]::new);

        assertArrayEquals(new String[]{"root", "left", "right",
                        "left-left", "left-right",
                        "right-left", "right-right"},
                result);
    }

    @Test
    public void addresses() {
        Company company = Company.builder().departments(new HashMap<>()).build();
        Address addr = Address.builder().build();
        City ny = City.builder()
                .name("New York")
                .mayor(Person.builder()
                        .name("Rudi")
                        .addresses(new Address[]{addr})
                        .build())
                .build();
        addr.setCity(ny);
        addr.setStreet("House St.");
        company.getDepartments().put("software",
                Department.builder()
                        .boss(Person.builder()
                                .name("Bill")
                                .build())
                        .employees(Collections.singletonList(
                                Person.builder()
                                        .name("John")
                                        .addresses(new Address[]{Address.builder()
                                                .street("Wall St.")
                                                .city(ny)
                                                .build()})
                                        .build()))
                        .employees(Collections.singletonList(
                                Person.builder()
                                        .name("Mike")
                                        .addresses(new Address[]{Address.builder()
                                                .street("Long St.")
                                                .city(ny)
                                                .build()})
                                        .build()))
                        .build());
        company.getDepartments().put("hardware",
                Department.builder()
                        .boss(Person.builder()
                                .name("Steve")
                                .build())
                        .employees(Arrays.asList(
                                Person.builder()
                                        .name("Kylie")
                                        .addresses(new Address[]{Address.builder()
                                                .street("5th Ave.")
                                                .city(ny)
                                                .build()})
                                        .build(),
                                Person.builder()
                                        .name("Paul")
                                        .addresses(new Address[]{Address.builder()
                                                .street("Downing St.")
                                                .city(City.builder().name("London").build())
                                                .build()})
                                        .build()))
                        .build());
        company.getDepartments().put("sales",
                Department.builder()
                        .boss(Person.builder()
                                .name("Donald")
                                .build())
                        .employees(Collections.singletonList(
                                Person.builder()
                                        .name("Margareth")
                                        .addresses(new Address[]{Address.builder()
                                                .street("Hyde Park")
                                                .city(City.builder().name("London").build())
                                                .build(),
                                                Address.builder()
                                                        .street("Short St.")
                                                        .city(ny)
                                                        .build()})
                                        .build()))
                        .build());

        String[] names =
                from(company)
                        .find(Department.class)
                        .filter(d -> !d.getBoss().getName().equals("Bill"))
                        .find(Person.class)
                        .filter(p -> from(p).find(City.class).extract(City::getMayor).stream().anyMatch(m -> m.getName().equals("Rudi")))
                        .then(e -> e.extract(Person::getName),
                                e -> e.extract(p -> p.getAddresses()[0]).extract(Address::getStreet))
                        .stream()
                        .toArray(String[]::new);


        assertArrayEquals(new String[]{"Margareth", "Hyde Park", "Kylie", "5th Ave.",}, names);
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
    public static class Company {
        private Map<String, Department> departments;
    }

    @Data
    @Builder
    public static class Department {
        private Person boss;
        private List<Person> employees;
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
        String name;
        Person mayor;
    }
}