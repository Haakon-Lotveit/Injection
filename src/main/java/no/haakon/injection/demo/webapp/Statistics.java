package no.haakon.injection.demo.webapp;

import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Oppretter noen enkle metrikker til tjenesten, for å demonstrere
 */
public class Statistics {
    private static final Logger log = LoggerFactory.getLogger(Statistics.class);

    LinkedBlockingDeque<UpcaseInstance> loggedRequests = new LinkedBlockingDeque<>();
    public void receiveCase(Context context) {
        String request = context.pathParam("word");
        logRequest(request);
        log.info("Har {} forespørsler", loggedRequests.size());
    }

    private void logRequest(String request) {
        LocalDateTime when = LocalDateTime.now();
        int length = request.length();
        long alpha = request.chars().filter(Character::isAlphabetic).count();
        long nonAlpha = length - alpha;
        long downCasedLetters = request.chars().filter(Character::isLowerCase).count();
        long upcasedLetters = request.chars().filter(Character::isUpperCase).count();

        loggedRequests.add(new UpcaseInstance(when, length, nonAlpha, alpha, downCasedLetters, upcasedLetters));
    }

    public Collection<UpcaseInstance> snapshot() {
        return new ArrayList<>(loggedRequests);
    }
}
