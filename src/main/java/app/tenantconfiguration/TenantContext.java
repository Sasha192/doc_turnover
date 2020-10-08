package app.tenantconfiguration;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class TenantContext {

    public static final String DEFAULT_TENANT_IDENTIFIER = "bcrew_default";

    private static final ThreadLocal<String> TENANT_IDENTIFIER =
            new ThreadLocal<>();

    public static void setTenant(String tenantIdentifier) {
        TENANT_IDENTIFIER.set(tenantIdentifier);
    }

    public static void reset(String tenantIdentifier) {
        TENANT_IDENTIFIER.remove();
    }

    public static class TenantIdentifierResolver
            implements CurrentTenantIdentifierResolver {

        @Override
        public String resolveCurrentTenantIdentifier() {
            String currentTenantId = TENANT_IDENTIFIER.get();
            return currentTenantId != null
                    ? currentTenantId :
                    DEFAULT_TENANT_IDENTIFIER;
        }

        @Override
        public boolean validateExistingCurrentSessions() {
            return false;
        }
    }
}
