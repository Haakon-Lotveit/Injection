package no.haakon.injection.injector;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public interface Injector {

    <T> void bindInstance(Class<T> type, T instance);
    <T> void bindProvider(Class<T> type, Supplier<T> provider);

    <T> T resolveInstance(Class<T> type);
    <T> Supplier<T> resolveProvider(Class<T> type);
}
