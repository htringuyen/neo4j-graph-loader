package org.snowj.synthea.ingest.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TimeCounter {
    private static final Logger logger = LoggerFactory.getLogger(TimeCounter.class);

    private Instant startInstant;
    private final List<Duration> elapsedDurations = new ArrayList<>();
    private boolean started = false;



    public void start() {
        if (! started) {
            started = true;
            startInstant = Instant.now();
        }
        startInstant = Instant.now();
    }

    public Duration stop() {
        if (! started) {
            logger.warn("TimeCounter.stop() called before TimeCounter.start()");
            return Duration.ZERO;
        }
        started = false;
        var elapsedDuration = Duration.between(startInstant, Instant.now());
        elapsedDurations.add(elapsedDuration);
        return elapsedDuration;
    }

    public Duration getTotalDuration() {
        return elapsedDurations.stream().reduce(Duration.ZERO, Duration::plus);
    }

    public void stopAndLogElapsedTime() {
        var duration = stop();
        logger.info("Elapsed time: {} seconds", duration.getSeconds());
    }

    public void countAndLogTotalElapsedTime() {
        logger.info("Total elapsed time: {} seconds", getTotalDuration().getSeconds());
    }

}
