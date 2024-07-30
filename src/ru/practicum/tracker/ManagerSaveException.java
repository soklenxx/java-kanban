package ru.practicum.tracker;

public class ManagerSaveException extends RuntimeException {

    private static final String MSG_SAVE = "Error occurred while saving";
    private static final String MSG_LOAD = "Error occurred while saving";

    public ManagerSaveException(String msg, Exception e) {
        super(msg, e);
    }
    public static ManagerSaveException saveException(Exception e) {
        return new ManagerSaveException(MSG_SAVE, e);
    }

    public static ManagerSaveException loadException(Exception e) {
        return new ManagerSaveException(MSG_LOAD, e);
    }
}
