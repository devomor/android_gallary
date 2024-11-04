package com.photo.photography.progress;

public class ExceptionProgress extends Exception {

    private final ErrorsCause error;

    public ExceptionProgress(ErrorsCause error) {
        this.error = error;
    }

    public ExceptionProgress(String error) {
        this.error = new ErrorsCause(error);
    }

    public ErrorsCause getError() {
        return error;
    }

    @Override
    public String toString() {
        return error.toString();
    }

    @Override
    public String getMessage() {
        return toString();
    }

    @Override
    public String getLocalizedMessage() {
        return toString();
    }
}
