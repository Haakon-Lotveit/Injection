package no.haakon.injection.injector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Første enkleste innsprøyteren vi definerer.
 */
@SuppressWarnings("unused")
public class BasicInjector implements Injector {
    private final static Logger log = LoggerFactory.getLogger(BasicInjector.class);

    private final Map<Class<?>, Object> instances;
    private final Map<Class<?>, Supplier<?>> providers;

    private BasicInjector(Map<Class<?>, Object> instances, Map<Class<?>, Supplier<?>> providers) {
        Objects.requireNonNull(instances, "Map over instances cannot be null. (Empty is a-ok)");
        Objects.requireNonNull(providers, "Map over providers cannot be null. (Empty is a-ok)");
        this.instances = instances;
        this.providers = providers;
    }

    /**
     * Oppretter en ny tom innsprøyter
     */
    public BasicInjector() {
        this(new HashMap<>(), new HashMap<>());
    }

    @Override
    public <T> void bindInstance(Class<T> type, T instance) {
        if (instances.containsKey(type)) {
            log.warn("Overriding existing binding for type " + type);
        }
        instances.put(type, instance);
    }

    @Override
    public <T> void bindProvider(Class<T> type, Supplier<T> provider) {
        if (providers.containsKey(type)) {
            log.warn("Overriding existing binding for type " + type);
        }
        providers.put(type, provider);
    }

    @Override
    public <T> T resolveInstance(Class<T> type) {
        return (T) instances.get(type);
    }

    @Override
    public <T> Supplier<T> resolveProvider(Class<T> type) {
        return (Supplier<T>) providers.get(type);
    }
}
