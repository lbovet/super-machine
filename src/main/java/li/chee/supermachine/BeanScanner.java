package li.chee.supermachine;

import li.chee.supermachine.traverser.Traverser;

import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * A scanner traversing an object graph.
 * @param <T> the type of the traversed items.
 */
public class BeanScanner<T> extends Scanner<T> {

    /**
     * Creates a bean scanner.
     * @param root the root object from which the graph will be traversed.
     * @param <X> the type of the root object.
     * @return a scanner for the root object.
     */
    public static <X> Scanner<X> from(X root) {
        return new BeanScanner<>(Stream.of(root));
    }

    @Override
    protected <X> Scanner<X> create(Stream<X> source) {
        return new BeanScanner<>(source);
    }

    private BeanScanner(Stream<T> stream) {
        super(stream);
    }

    /**
     * Traverses the object graph for each items of this scanner and returns a scanner
     * emitting all found items of the given type.
     * @param clazz the type selector.
     * @param <X> type of the selector and resulting items.
     * @return a scanner emitting the selected items.
     */
    @Override
    public <X> Scanner<X> walk(Class<X> clazz) {
        return new BeanScanner<>(stream().flatMap(x -> traverse(x, null))).superFind(clazz);
    }

    /**
     * Traverses the object graph for each items of this scanner and returns a scanner
     * emitting the first item of the given type found in each branch.
     * @param clazz the type selector.
     * @param <X> type of the selector and resulting items.
     * @return a scanner emitting the selected items.
     */
    @Override
    public <X> Scanner<X> find(Class<X> clazz) {
        return new BeanScanner<>(stream().flatMap(x -> traverse(x, clazz))).superFind(clazz);
    }

    private <X> Scanner<X> superFind(Class<X> clazz) {
        return super.walk(clazz);
    }

    private <X> Stream<Object> traverse(T root, Class<X> stopClass) {
        LinkedList<Object> list = new LinkedList<>();
        Traverser.traverse(root, null, stopClass, list::add);
        return list.stream();
    }
}
