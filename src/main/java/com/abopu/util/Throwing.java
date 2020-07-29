package com.abopu.util;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * @author Sarah Skanes &lt;mirrana@abopu.com&gt;
 */
public class Throwing {

    private Throwing() {}

    @NotNull
    public static <T> Consumer<T> rethrow(@NotNull final ThrowingConsumer<T> consumer) {
        return consumer;
    }

    /**
     * The compiler sees the signature with the throws T inferred to a RuntimeException type, so it
     * allows the unchecked exception to propagate.
     * <p>
     * http://www.baeldung.com/java-sneaky-throws
     */
    @SuppressWarnings("unchecked")
    public static <E extends Throwable> void sneakyThrow(@NotNull Throwable ex) throws E {
        throw (E) ex;
    }
}
