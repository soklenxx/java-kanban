package ru.practicum.tracker;

public class ManagerSaveExcepption extends RuntimeException {

    private static final String MSG_SAVE = "Error occurred while saving";
    private static final String MSG_LOAD = "Error occurred while saving";
    public ManagerSaveExcepption (String msg, Exception e) {
        super(msg, e);
    }
    public static ManagerSaveExcepption saveExcepption (Exception e) {
        return new ManagerSaveExcepption(MSG_SAVE, e);
    }

    public static ManagerSaveExcepption loadExcepption (Exception e) {
        return new ManagerSaveExcepption(MSG_LOAD, e);
    }
}
