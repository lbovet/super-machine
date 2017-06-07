package li.chee.supermachine;

import java.util.stream.Stream;

/**
 * A scanner iterating over items of a stream. Nothing really fancy.
 * @param <X> the types of the stream items.
 */
public class StreamScanner<X> extends Scanner<X> {

    /**
     * Create a scanner iterating over a stream.
     * @param stream the stream to scan.
     * @param <X> the type of stream items.
     * @return
     */
    public static <X> Scanner<X> scan(Stream<X> stream) {
        return new StreamScanner<>(stream);
    }

    @Override
    protected <X> Scanner<X> create(Stream<X> source) {
        return new StreamScanner<X>(source);
    }

    private StreamScanner(Stream<X> stream) {
        super(stream);
    }
}
