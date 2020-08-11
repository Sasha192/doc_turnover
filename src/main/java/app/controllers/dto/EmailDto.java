package app.controllers.dto;

import javax.validation.constraints.NotNull;

public class EmailDto {
    @NotNull
    private String[] docList;
    @NotNull
    private String message;
    @NotNull
    private String email;

    public String[] getDocList() {
        return docList;
    }

    public void setDocList(String[] docList) {
        this.docList = docList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
