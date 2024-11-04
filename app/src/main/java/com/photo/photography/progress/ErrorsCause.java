package com.photo.photography.progress;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ErrorsCause {

    private final String title;
    private final ArrayList<String> causes;

    public ErrorsCause(String title, ArrayList<String> causes) {
        this.title = title;
        this.causes = causes;
    }

    public ErrorsCause(String title) {
        this.title = title;
        this.causes = new ArrayList<>(1);
    }

    public static ErrorsCause fromThrowable(Throwable throwable) {
        if (throwable instanceof ExceptionProgress)
            return ((ExceptionProgress) throwable).getError();
        else return new ErrorsCause(throwable.getMessage());
    }

    public void addCause(String cause) {
        this.causes.add(cause);
    }

    public String getTitle() {
        return title;
    }

    public boolean hasErrors() {
        return causes.size() > 0;
    }

    public @Nullable
    ErrorsCause get() {
        if (hasErrors()) return this;
        else return null;
    }

    public ArrayList<String> getCauses() {
        return causes;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(title).append("\n");

        for (String cause : causes) {
            b.append(cause).append("\n");
        }

        return b.toString();
    }
}
