package app.tenantdefault.models;

import app.controllers.dto.TenantDto;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity("tenant_info")
public class TenantInfoEntity implements Serializable {

    public static final String COLLECTION_NAME = "tenant_info";

    @Id
    private String tenantId;

    private String companyName;

    private Long ownerId;

    private String companyAddress;

    private PaymentTariff tariff;

    private boolean privateAccount;

    private java.util.Date creationDate;

    public TenantInfoEntity() {
    }

    public TenantInfoEntity(String tenantId) {
        this.tenantId = tenantId;
    }

    public TenantInfoEntity(TenantDto dto) {
        setCompanyAddress(dto.getCompanyAddress());
        setCompanyName(dto.getCompanyName());
        PaymentTariff tariff = Enum.valueOf(
                PaymentTariff.class, dto.getPaymentTariff()
        );
        setTariff(tariff);
        creationDate = java.util.Calendar
                .getInstance()
                .getTime();
        setPrivateAccount(dto.isPrivate());
        String invite = dto.getCompanyName().hashCode()
                + UUID.randomUUID().toString()
                .replace("-", "");
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public PaymentTariff getTariff() {
        return tariff;
    }

    public void setTariff(PaymentTariff tariff) {
        this.tariff = tariff;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setPrivateAccount(boolean privateAccount) {
        this.privateAccount = privateAccount;
    }

    public boolean isPrivateAccount() {
        return privateAccount;
    }
}
