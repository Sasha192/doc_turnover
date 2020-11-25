package app.configuration.spring;

import app.tenantconfiguration.TenantConnectionProvider;
import app.tenantconfiguration.TenantContext;

import java.util.Collection;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import app.tenantdefault.service.ITenantService;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.Interceptor;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
@PropertySource("classpath:app.properties")
@EnableTransactionManagement
@EnableAspectJAutoProxy
@ComponentScans({
        @ComponentScan("app.security.authenticator"),
        @ComponentScan("app.customtenant.dao"),
        @ComponentScan("app.customtenant.models"),
        @ComponentScan("app.security.models"),
        @ComponentScan("app.security.dao"),
        @ComponentScan("app.security.service"),
        @ComponentScan("app.configuration.spring.appfactory"),
        @ComponentScan("app.configuration.spring.constants"),
        @ComponentScan("app.configuration.spring.hibernateinterceptors"),
        @ComponentScan("app.utils"),
        @ComponentScan("app.customtenant.statisticsmodule"),
        @ComponentScan("app.customtenant.eventdriven"),
        @ComponentScan("app.customtenant.events"),
        @ComponentScan("app.customtenant.service"),
        @ComponentScan("app.tenantconfiguration"),
})
@Import({AsyncConfig.class, FlyWayConfig.class,
        MongoConfiguration.class,
        SpringMvcConfiguration.class,
        WebSocketConfiguration.class,
        JmsActiveMQConfiguration.class
})
public class SpringDataConfiguration {

    private final Environment env;
    private final Interceptor interceptor;
    private final ITenantService tenantService;

    public SpringDataConfiguration(Environment env,
                                   @Qualifier("hibernate_empty_int_impl")
                                           Interceptor interceptor,
                                   ITenantService tenantService) {
        this.env = env;
        this.interceptor = interceptor;
        this.tenantService = tenantService;
    }

    @Bean("bcrew_default_data_source")
    public DataSource defaultDataSource() {
        final BasicDataSource dataSource = new BasicDataSource();
        String url = env.getProperty("db.abstract_url");
        url = url.replaceAll("\\{db_name\\}", "bcrew_default");
        dataSource.setDriverClassName(this.env.getProperty("db.driver"));
        dataSource.setUrl(url);
        dataSource.setUsername(this.env.getProperty("db.username"));
        dataSource.setPassword(this.env.getProperty("db.password"));
        return dataSource;
    }

    @Bean("phantom_data_source")
    public DataSource phantomTenant() {
        final BasicDataSource dataSource = new BasicDataSource();
        String url = env.getProperty("db.abstract_url");
        url = url.replaceAll("\\{db_name\\}", "bcrew");
        dataSource.setDriverClassName(this.env.getProperty("db.driver"));
        dataSource.setUrl(url);
        dataSource.setUsername(this.env.getProperty("db.username"));
        dataSource.setPassword(this.env.getProperty("db.password"));
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setPackagesToScan("app.customtenant.models",
                "app.security.models",
                "app.customtenant.statisticsmodule",
                "app.customtenant.events");
        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        Properties hibernateProperties = hibernateProperties();
        Collection<String> tenants = tenantService.findTenants();
        TenantConnectionProvider tenantConnectionProvider =
                multiTenantConnectionProvider(tenants, hibernateProperties);
        hibernateProperties.put(
                org.hibernate.cfg.Environment.MULTI_TENANT_CONNECTION_PROVIDER,
                tenantConnectionProvider);
        hibernateProperties.put(AvailableSettings.INTERCEPTOR, interceptor);
        em.setJpaProperties(hibernateProperties);

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public SessionLocaleResolver getLocaleResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        String lang = env.getProperty("app.locale.lang");
        String country = env.getProperty("app.locale.country");
        slr.setDefaultLocale(new Locale(lang, country));
        String timeZone = env.getProperty("app.timeZone");
        slr.setDefaultTimeZone(TimeZone.getTimeZone(timeZone));
        return slr;
    }

    @Bean(value = "tenant_conn_provider")
    public TenantConnectionProvider multiTenantConnectionProvider(Collection<String> tenants, Properties properties) {
        return new TenantConnectionProvider(defaultDataSource(), phantomTenant(), tenants, env, properties);
    }

    private Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto",
                this.env.getProperty("hibernate.hbm2ddl.auto"));
        hibernateProperties.setProperty("hibernate.dialect",
                this.env.getProperty("hibernate.dialect"));
        hibernateProperties.setProperty("hibernate.enable_lazy_load_no_trans",
                this.env.getProperty("hibernate.enable_lazy_load_no_trans"));
        hibernateProperties.setProperty("hibernate.proc.param_null_passing",
                String.valueOf(true));
        hibernateProperties.setProperty("hibernate.show_sql",
                env.getProperty("hibernate.show_sql"));
        hibernateProperties.put(org.hibernate.cfg.Environment.MULTI_TENANT,
                MultiTenancyStrategy.SCHEMA);
        hibernateProperties.setProperty(
                AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER,
                TenantContext.TenantIdentifierResolver.class.getName()
        );
        /*hibernateProperties.setProperty("hibernate.jdbc.batch_size",
                env.getProperty("hibernate.jdbc.batch_size"));
        hibernateProperties.setProperty("hibernate.order_inserts",
                env.getProperty("hibernate.order_inserts"));*/
        /*hibernateProperties.setProperty("hibernate.order_updates",
                env.getProperty("hibernate.order_updates"));
        hibernateProperties.setProperty("hibernate.batch_versioned_data",
                env.getProperty("hibernate.batch_versioned_data"));*/
        return hibernateProperties;
    }
}
