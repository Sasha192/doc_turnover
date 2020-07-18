package app.security.models;

import app.models.VerificationCode;
import app.security.models.annotations.FieldsValueMatch;
import app.security.models.annotations.ValidPasswordPattern;
import com.google.gson.annotations.Expose;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class UserDto {

    public interface New {
    }

    public interface Exist {
    }

    public interface Auth {

    }

    public interface UpdatePassword extends Exist {
    }

    @Expose
    private @Null(groups = {New.class, Auth.class}) @NotNull(groups = {UpdatePassword.class, Exist.class}) Long id;

    @Expose
    private @NotNull(groups = {New.class, Auth.class}) @Null(groups = UpdatePassword.class) @Email String email;

    @ValidPasswordPattern(groups = {New.class, UpdatePassword.class})
    private @NotNull(groups = {New.class, UpdatePassword.class, Auth.class}) String password;

    private @NotNull Boolean remember;

    private transient String verificationCode;

    private transient long creationTime;

    private transient long verificationId;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public void setVerificationCode(VerificationCode code) {
        this.setVerificationId(code.getId());
        this.setVerificationCode(code.getCode());
        this.setCreationTime(code.getCreationtime());
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public long getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(long verificationId) {
        this.verificationId = verificationId;
    }

    public Boolean getRemember() {
        return this.remember;
    }

    public void setRemember(final Boolean remember) {
        this.remember = remember;
    }
}
