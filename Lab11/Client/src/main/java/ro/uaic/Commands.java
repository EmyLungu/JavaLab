package ro.uaic;

public enum Commands {
    JOIN_GAME("<|join_game|>"),
    ANSWER("<|anwer|>"),
    STOP_CLIENT("<|stop_client|>"),
    QUERY("<|query|>");

    private final String text;

    Commands(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
