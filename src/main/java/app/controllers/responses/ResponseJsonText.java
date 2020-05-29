package app.controllers.responses;

public class ResponseJsonText {

    private boolean success;
    private String msg;

    public ResponseJsonText(final boolean success, final String message) {
        this.success = success;
        this.msg = message;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(final boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(final String msg) {
        this.msg = msg;
    }
}
