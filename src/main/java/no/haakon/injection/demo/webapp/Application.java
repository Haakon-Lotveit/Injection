package no.haakon.injection.demo.webapp;

import io.javalin.http.Handler;
import no.haakon.injection.injector.InjectorWithNaming;
import no.haakon.injection.injector.NamingInjector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Application {
    public static void main(String[] args) {
        InjectorWithNaming innsprøyter = initializeBeans();

        WebApp app = innsprøyter.inject(WebApp.class);
        app.start();
    }

    private static InjectorWithNaming initializeBeans() {
        NamingInjector innsprøyter = new NamingInjector();
        {
            File propertiesFil = new File("webapp-demo/Settings.properties");
            try (FileInputStream fis = new FileInputStream(propertiesFil)) {
                Properties innstillinger = new Properties();
                innstillinger.load(fis);
                innsprøyter.bindInstance(Properties.class, innstillinger);
            } catch (IOException e) {
                System.err.printf("Kunne ikke laste inn properties. Sjekk at filen %s finnes og at den kan leses.%n",
                        propertiesFil);
                System.exit(1); // Vi krasjer her. Ingen vits i å fortsette.
            }
        }

        innsprøyter.bindInstance(Handler.class, "upcaser",
                (context) -> context.result(context.pathParam("word").toUpperCase()));
        innsprøyter.bindInstance(Handler.class, "downcaser",
                (context) -> context.result(context.pathParam("word").toLowerCase()));

        innsprøyter.bindClass(Statistics.class);
        innsprøyter.bindClass(BiModule.class);
        return innsprøyter;
    }
}
