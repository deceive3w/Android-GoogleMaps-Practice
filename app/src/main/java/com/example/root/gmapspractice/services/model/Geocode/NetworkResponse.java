package com.example.root.gmapspractice.services.model.Geocode;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class NetworkResponse<T> {

    public STATUS status = null;

    @Nullable
    public T data = null;

    @Nullable
    public Throwable error = null;

    private NetworkResponse(STATUS status, @Nullable T data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static NetworkResponse loading() {
        return new NetworkResponse(STATUS.LOADNIG, null, null);
    }

    public static NetworkResponse success(@NonNull Object data) {
        return new NetworkResponse(STATUS.SUCCESS, data, null);
    }

    public static NetworkResponse error(@NonNull Throwable error) {
        return new NetworkResponse(STATUS.ERROR, null, error);
    }
    public enum STATUS{
        LOADNIG,SUCCESS,ERROR;
    }
}