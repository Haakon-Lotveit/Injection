package no.haakon.injection.injector;

import no.haakon.injection.annotation.Inject;
import no.haakon.injection.exception.InjectionException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class NamingInjector implements InjectorWithNaming {
    Map<Class<?>, Map<String, Object>>      instances;
    Map<Class<?>, Map<String, Supplier<?>>> providers;

    private NamingInjector(Map<Class<?>, Map<String, Object>> instances,
                           Map<Class<?>, Map<String, Supplier<?>>> providers) {
        this.instances = instances;
        this.providers = providers;
    }

    public NamingInjector() {
        this(new HashMap<>(), new HashMap<>());
    }


    @Override
    public <T> void bindInstance(Class<T> type, String name, T instance) {
        instances.computeIfAbsent(type, __ -> new HashMap<>()).put(name, instance);
    }

    @Override
    public <T> void bindProvider(Class<T> type, String name, Supplier<T> provider) {
        providers.computeIfAbsent(type, __ -> new HashMap<>()).put(name, provider);
    }

    @Override
    public <T> T resolveInstance(Class<T> type, String name) {
        return (T) instances.getOrDefault(type, Collections.emptyMap()).get(name);
    }

    @Override
    public <T> Supplier<T> resolveProvider(Class<T> type, String name) {
        return (Supplier<T>) providers.getOrDefault(type, Collections.emptyMap()).get(name);
    }

    @Override
    public <T> T inject(Class<T> type) {
        return tryConstructorInjection(type)
                .or(() -> tryFieldInjection(type))
                .orElseThrow(InjectionException::new)
                .resolve();
    }

    private <T> Optional<ConstructorResolver<T>> tryConstructorInjection(Class<T> type) {
        for (Constructor<?> cons : type.getConstructors()) {
            if (cons.isAnnotationPresent(Inject.class)) {
                // Hvis vi finner en annotert konstruktør bruker vi den.
                Constructor<T> chosenConstructor = (Constructor<T>) cons;
                Class<?>[] typer = chosenConstructor.getParameterTypes();
                Annotation[][] annotasjoner = chosenConstructor.getParameterAnnotations();
                Object[] argumenter = new Object[typer.length];
                for (int i = 0; i < typer.length; ++i) {
                    String instansNavn = Arrays.stream(annotasjoner[i])
                            .filter(Inject.class::isInstance)
                            .findAny()
                            .map(Inject.class::cast)
                            .map(Inject::value)
                            .orElse(DEFAULT_NAME);
                    argumenter[i] = this.resolveInstance(typer[i], instansNavn);
                }
                return constructorInvoker(chosenConstructor, argumenter);
            }
        }
        return Optional.empty();
    }

    private <T> Optional<ConstructorResolver<T>> constructorInvoker(Constructor<T> chosenConstructor, Object[] argumenter) {
        return Optional.of(() -> {
            try {
                return chosenConstructor.newInstance(argumenter);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new InjectionException();
            }
        });
    }

    private <T> Optional<ConstructorResolver<T>> tryFieldInjection(Class<T> type) {
        return Arrays.stream(type.getDeclaredConstructors())
                .filter(c -> c.getParameterCount() == 0)
                .findAny()
                .map(c -> (Constructor<T>) c)
                .map(this::fieldInjector);
    }

    private <T> ConstructorResolver<T> fieldInjector(Constructor<T> chosenConstructor) {
        return () -> {
            try {
                chosenConstructor.setAccessible(true);
                T instance = chosenConstructor.newInstance();
                for (Field f : instance.getClass().getDeclaredFields()) {
                    Inject annotation = f.getAnnotation(Inject.class);
                    if (null != annotation) {
                        f.setAccessible(true);
                        f.set(instance, this.resolveInstance(f.getType(), annotation.value()));
                    }
                }
                return instance;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new InjectionException();
            }};
    }
}
