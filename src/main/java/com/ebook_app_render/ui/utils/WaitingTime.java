package com.ebook_app_render.ui.utils;

import java.time.Duration;

public enum WaitingTime {
    WAITING_TIME(90);

    private final int inSeconds;

    WaitingTime(int inSeconds) {
        this.inSeconds = inSeconds;
    }

    public Duration getDuration() {
        return Duration.ofSeconds(inSeconds);
    }
}
