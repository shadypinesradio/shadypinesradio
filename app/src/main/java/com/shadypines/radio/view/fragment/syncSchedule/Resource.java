package com.shadypines.radio.view.fragment.syncSchedule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
public class Resource<T> {
    @Nullable
    private T data;
    @Nullable
    private Throwable error;
    @Nullable
    private String day;

    private Resource(@Nullable T data, @Nullable Throwable error, @Nullable String day) {
        this.data = data;
        this.error = error;
        this.day = day;
    }

    public static <T> Resource<T> success(@NonNull T data, String day) {
        return new Resource<>(data, null, day);
    }

    public static <T> Resource<T> error(@NonNull Throwable error) {
        return new Resource<>(null, error, null);
    }

    @Nullable
    public T getData() {
        return data;
    }

    @Nullable
    public Throwable getError() {
        return error;
    }

    @Nullable
    public String getDay() {
        return day;
    }
}
