package uk.co.magictractor.util.exception;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;

// Copied from Spew project.
public final class ExceptionUtil {

    private ExceptionUtil() {
    }

    public static RuntimeException notYetImplemented() {
        return new UnsupportedOperationException("Not yet implemented");
    }

    public static <E extends Exception> void call(RunnableWithThrowable runnable) {
        call(() -> {
            runnable.run();
            return null;
        });
    }

    public static <VALUE> VALUE call(CallableWithThrowable<VALUE> callable) {
        try {
            return callable.call();
        }
        catch (InvocationTargetException e) {
            throw unwrap(e);
        }
        catch (Exception e) {
            throw asRuntimeException(e);
        }
        catch (Error e) {
            throw e;
        }
        catch (Throwable e) {
            throw new IllegalStateException("Throwable is not an Exception or Error", e);
        }
    }

    private static RuntimeException unwrap(Throwable e) {
        if (e instanceof InvocationTargetException) {
            return (unwrap(e.getCause()));
        }
        return asRuntimeException(e);
    }

    private static RuntimeException asRuntimeException(Throwable e) {
        return asRuntimeException(null, e);
    }

    public static RuntimeException asRuntimeException(String message, Throwable e) {
        if (e instanceof RuntimeException) {
            // hmm, message lost...
            return (RuntimeException) e;
        }
        else if (e instanceof Error) {
            // hmm, message lost...
            throw (Error) e;
        }
        else if (e instanceof IOException) {
            return new UncheckedIOException(message, (IOException) e);
        }
        return new IllegalStateException(message, e);
    }

    @FunctionalInterface
    public interface RunnableWithThrowable {
        void run() throws Throwable;
    }

    @FunctionalInterface
    public interface CallableWithThrowable<VALUE> {
        VALUE call() throws Throwable;
    }

    @FunctionalInterface
    public static interface FunctionWithException<T, R, E extends Exception> {
        R apply(T t) throws E;
    }

    @FunctionalInterface
    public static interface BiFunctionWithException<T, U, R, E extends Exception> {
        R apply(T t, U u) throws E;
    }

    @FunctionalInterface
    public static interface ConsumerWithException<T, E extends Exception> {
        void accept(T t) throws E;
    }

    @FunctionalInterface
    public interface SupplierWithException<T, E extends Exception> {
        T get() throws E;
    }

}
