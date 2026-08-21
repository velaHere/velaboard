package org.velas.scoreboard.core.exception;

public final class ScoreboardException extends RuntimeException{

    public ScoreboardException(String message){
        super(message);
    }

    public ScoreboardException(String message, Throwable cause) {
        super(message, cause);
    }
}
