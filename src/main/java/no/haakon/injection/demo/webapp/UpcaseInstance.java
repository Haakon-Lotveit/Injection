package no.haakon.injection.demo.webapp;

import java.time.LocalDateTime;

@SuppressWarnings("unused")
public class UpcaseInstance {
    LocalDateTime when;
    long length;
    long nonAlpha;
    long alpha;
    long downCasedLetters;
    long upcasedLetters;

    public UpcaseInstance(LocalDateTime when, long length, long nonAlpha, long alpha, long downCasedLetters, long upcasedLetters) {
        this.when = when;
        this.length = length;
        this.nonAlpha = nonAlpha;
        this.alpha = alpha;
        this.downCasedLetters = downCasedLetters;
        this.upcasedLetters = upcasedLetters;
    }

    public LocalDateTime getWhen() {
        return when;
    }

    public long getLength() {
        return length;
    }

    public long getNonAlpha() {
        return nonAlpha;
    }

    public long getAlpha() {
        return alpha;
    }

    public long getDownCasedLetters() {
        return downCasedLetters;
    }

    public long getUpcasedLetters() {
        return upcasedLetters;
    }
}
