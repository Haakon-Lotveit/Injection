package no.haakon.injection.injector;

@FunctionalInterface
public interface ConstructorResolver<T> {
    T resolve();
}
