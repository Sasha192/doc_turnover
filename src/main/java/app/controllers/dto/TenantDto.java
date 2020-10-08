package app.controllers.dto;

import javax.validation.constraints.NotNull;

public class TenantDto {

    @NotNull
    private String companyName;

    @NotNull
    private String companyAddress;

    @NotNull
    private String paymentTariff;

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public String getPaymentTariff() {
        return paymentTariff;
    }
}
