package app.customtenant.messenger.models;

public final class WSocketPostMessage {

    private final String message;
    private final String username;

    public WSocketPostMessage(String message,
                                String username) {
        this.message = message;
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public String getTarget() {
        return username;
    }
}
