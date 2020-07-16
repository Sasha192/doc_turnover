package app.security.models;

import app.models.serialization.ExcludeThis;
import app.security.models.annotations.ExcludeMatchingPassword;
import app.security.models.annotations.FieldsValueMatch;
import app.security.models.annotations.ValidPasswordPattern;
import com.google.gson.annotations.Expose;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@FieldsValueMatch(
        field = "password",
        fieldMatch = "matchingPassword",
        message = "Passwords do not match!",
        groups = {UserDto.New.class, UserDto.UpdatePassword.class}
)
public class UserDto {

    public interface New {
    }

    public interface Exist {
    }

    public interface Auth {

    }

    public interface UpdatePassword extends Exist {
    }

    @Null(groups = {New.class, Auth.class})
    @NotNull(groups = {UpdatePassword.class, Exist.class})
    @Expose
    private Long id;

    @NotNull(groups = {New.class, Auth.class})
    @Null(groups = {UpdatePassword.class})
    @Expose
    @Email
    private String email;

    @NotNull(groups = {New.class, UpdatePassword.class, Auth.class})
    @ValidPasswordPattern(groups = {New.class, UpdatePassword.class})
    private String password;

    @NotNull(groups = {New.class, UpdatePassword.class})
    @Null(groups = {Auth.class})
    private String matchingPassword;

    @Null
    private transient String verificationCode;

    @Null
    private transient long creationTime;

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return this.matchingPassword;
    }

    public void setMatchingPassword(final String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getVerificationCode() {
        return this.verificationCode;
    }

    public void setVerificationCode(final String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public void setCreationTime(final long creationTime) {
        this.creationTime = creationTime;
    }
}
