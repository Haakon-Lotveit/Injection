package no.haakon.injection.demo.webapp;

import io.javalin.http.Context;
import no.haakon.injection.annotation.Inject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BiModule {
    @Inject
    private Statistics statistics;

    private BiModule() {}

    public void getStatsAsJson(Context context) {
        Collection<UpcaseInstance> snapshot = statistics.snapshot();
        Map<String, Object> output = new HashMap<>();
        int size = snapshot.size();
        long requestTotalLength = snapshot.stream().mapToLong(UpcaseInstance::getLength).sum();
        double averageRequestLength = ((double) requestTotalLength) / (double) size;

        output.put("numberOfRequests", size);
        output.put("averageRequestLength", averageRequestLength);
        output.put("shortestRequestLength", snapshot.stream().mapToLong(UpcaseInstance::getLength).min().orElse(0L));
        output.put("longestRequestLength", snapshot.stream().mapToLong(UpcaseInstance::getLength).max().orElse(0L));

        context.json(output);
    }
}
