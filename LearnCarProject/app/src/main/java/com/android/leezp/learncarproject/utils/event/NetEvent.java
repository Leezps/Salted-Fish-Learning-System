package com.android.leezp.learncarproject.utils.event;

public interface NetEvent {
    void onSuccess(Object object);
    void onError(String message);
}
