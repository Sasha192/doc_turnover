package app.controllers.responses;

public class ResponseJsonText {

    private Boolean success;
    private String message;

    public ResponseJsonText(final Boolean success, final String message) {
        this.success = success;
        this.message = message;
    }
}
