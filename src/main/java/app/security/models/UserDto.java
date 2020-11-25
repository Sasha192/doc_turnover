package app.security.models;

import app.security.models.annotations.ValidPasswordPattern;
import app.security.models.auth.VerificationCode;
import com.google.gson.annotations.Expose;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class UserDto {

    public boolean isLoginOperation() {
        return loginOperation;
    }

    public void setLoginOperation(boolean loginOperation) {
        this.loginOperation = loginOperation;
    }

    public interface New {
    }

    public interface Exist {
    }

    public interface Auth {
    }

    public interface JsonView {
    }

    public interface UpdatePassword extends Exist {
    }

    @Expose
    @Null(groups = {New.class, Auth.class})
    @NotNull(groups = {UpdatePassword.class, Exist.class})
    private Long id;

    @Expose
    @Null(groups = {UpdatePassword.class, New.class})
    @NotNull(groups = {Auth.class})
    @Email
    private String email;

    @ValidPasswordPattern(groups = {New.class, UpdatePassword.class})
    @NotNull(groups = {New.class, UpdatePassword.class, Auth.class})
    @Null(groups = {JsonView.class})
    private String passwd;

    @Null(groups = {Auth.class})
    private String firstName;

    @Null(groups = {Auth.class})
    private String lastName;

    @Null(groups = {Auth.class})
    private String middleName;

    private Boolean rememberMe;

    private transient String verificationCode;

    private transient long creationTime;

    private transient boolean loginOperation;

    public String getPassword() {
        return passwd;
    }

    public void setPassword(String password) {
        this.passwd = password;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
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

    public String getLogin() {
        return email;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public void setVerificationCode(VerificationCode code) {
        this.setVerificationCode(code.getCode());
        this.setCreationTime(code.getCreationtime());
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLogin(String login) {
        this.email = login;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
