package no.haakon.injection.demo.webapp;

import io.javalin.Javalin;
import io.javalin.http.Handler;
import no.haakon.injection.annotation.Inject;

import java.util.Properties;

public class WebApp {
    private final Properties settings;
    private final BiModule biModule;
    private final Statistics stats;
    private final Handler upcaser;
    private final Handler downcaser;

    @Inject
    public WebApp(Properties properties,
                  BiModule biModule,
                  Statistics stats,
                  @Inject("upcaser") Handler upcaser,
                  @Inject("downcaser") Handler downcaser) {

        this.settings = properties;
        this.stats = stats;
        this.biModule = biModule;
        this.upcaser = upcaser;
        this.downcaser = downcaser;
    }

    public void start() {
        Javalin app = Javalin.create().start(getPort());

        // ENDPOINTS
        app.get("/upcase/:word", upcaser);
        app.get("/downcase/:word", downcaser);

        app.get("/stats", biModule::getStatsAsJson);

        // BEFORE-HANDLERS / FILTERS
        app.before("/upcase/:word", stats::receiveCase);
        app.before("/downcase/:word", stats::receiveCase);
    }

    private int getPort() {
        return Integer.parseInt(settings.getProperty("port", "0"));
    }
}
