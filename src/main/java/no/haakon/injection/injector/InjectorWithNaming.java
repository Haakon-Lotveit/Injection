package no.haakon.injection.injector;

import java.util.function.Supplier;

public interface InjectorWithNaming extends Injector {
    String DEFAULT_NAME = "default";

    default <T> void bindInstance(Class<T> type, T instance) {
        bindInstance(type, DEFAULT_NAME, instance);
    }

    default <T> void bindProvider(Class<T> type, Supplier<T> provider) {
        bindProvider(type, DEFAULT_NAME, provider);
    }

    default <T> T resolveInstance(Class<T> type) {
        return resolveInstance(type, DEFAULT_NAME);
    }

    default <T> Supplier<T> resolveProvider(Class<T> type) {
        return resolveProvider(type, DEFAULT_NAME);
    }

    <T> void bindInstance(Class<T> type, String name, T instance);

    <T> void bindProvider(Class<T> type, String name, Supplier<T> provider);

    <T> T resolveInstance(Class<T> type, String name);

    <T> Supplier<T> resolveProvider(Class<T> type, String name);

    <T> T inject(Class<T> type);

    default <T> void bindClass(Class<T> type) {
        bindClass(type, DEFAULT_NAME);
    }

    default <T> void bindClass(Class<T> type, String name) {
        bindInstance(type, name, inject(type));
        bindProvider(type, name, () -> inject(type));
    }
}
