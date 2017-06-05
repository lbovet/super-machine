package li.chee.supermachine;

import li.chee.supermachine.traverser.Traverser;

import java.util.LinkedList;
import java.util.stream.Stream;

public class BeanScanner<T> extends Scanner<T> {

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

    @Override
    public <X> Scanner<X> walk(Class<X> clazz) {
        return new BeanScanner<>(stream().flatMap(x -> traverse(x, null))).superFind(clazz);
    }

    @Override
    public <X> Scanner<X> find(Class<X> clazz) {
        return new BeanScanner<>(stream().flatMap(x -> traverse(x, clazz))).superFind(clazz);
    }

    private <X> Scanner<X> superFind(Class<X> clazz) {
        return super.walk(clazz);
    }

    private <X> Stream<?> traverse(T root, Class<X> stopClass) {
        LinkedList<Object> list = new LinkedList<>();
        Traverser.traverse(root, null, stopClass, list::add);
        return list.stream();
    }
}
