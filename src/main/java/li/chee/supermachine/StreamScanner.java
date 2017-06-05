package li.chee.supermachine;

import java.util.stream.Stream;

public class StreamScanner<X> extends Scanner<X> {

    public static <X> Scanner<X> scan(Stream<X> stream) {
        return new StreamScanner<>(stream);
    }

    public StreamScanner(Stream<X> stream) {
        super(stream);
    }
}
