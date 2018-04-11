package ru.spbau.mit.javacourse.serializabletrie;

import java.io.IOException;

public class SerializedObjectFormatException extends IOException {
    public SerializedObjectFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
