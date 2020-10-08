package app.security.models.auth;

public final class VerificationCode {

    private Long id;
    private String code;
    private final long creationtime;

    public VerificationCode() {
        this.creationtime = System.currentTimeMillis();
    }

    public VerificationCode(String verificationKey, String verificationCode) {
        this.id = Long.valueOf(verificationCode.hashCode());
        this.code = verificationCode;
        this.creationtime = System.currentTimeMillis();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setId(final String id) {
        setId(Long.valueOf(id.hashCode()));
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public long getCreationtime() {
        return this.creationtime;
    }
}
