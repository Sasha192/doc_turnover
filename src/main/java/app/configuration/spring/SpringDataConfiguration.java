package app.configuration.spring;

import app.configuration.spring.constants.AppConstants;
import app.configuration.spring.constants.Constants;
import app.tenantconfiguration.TenantConnectionProvider;
import app.tenantconfiguration.TenantContext;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.Interceptor;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
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
        @ComponentScan("app.configuration"),
        @ComponentScan("app.utils"),
        @ComponentScan("app.customtenant.statisticsmodule"),
        @ComponentScan("app.customtenant.eventdriven"),
        @ComponentScan("app.customtenant.events"),
        @ComponentScan("app.customtenant.service"),
        @ComponentScan("app.tenantconfiguration"),
})
public class SpringDataConfiguration {

    @Autowired
    private Environment env;

    @Autowired
    @AppConstants
    private Constants constants;

    @Autowired
    private ApplicationContext context;

    @Autowired
    @Qualifier("hibernate_empty_int_impl")
    private Interceptor interceptor;

    @Bean("bcrew_default_data_source")
    public DataSource getDataSource() {
        final BasicDataSource dataSource = new BasicDataSource();
        String url = env.getProperty("db.abstract_url");
        url = url.replaceAll("\\{db_name\\}", "bcrew_default");
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
        //em.setDataSource(this.getDataSource());
        em.setPackagesToScan("app.customtenant.models",
                "app.security.models",
                "app.customtenant.statisticsmodule",
                "app.customtenant.events");
        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaPropertyMap(this.hibernateProperties());
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

    @Bean("tenant_conn_provider")
    public TenantConnectionProvider multiTenantConnectionProvider(Map<String, Object> props) {
        Properties properties = new Properties();
        props.entrySet().stream().forEach(
                (obj) -> {
                    properties.put(obj.getKey(), obj.getValue());
                }
        );
        return new TenantConnectionProvider(getDataSource(), env, properties);
    }

    public Map<String, Object> hibernateProperties() {
        /*Properties hibernateProperties = new Properties();*/
        Map<String, Object> hibernateProperties = new HashMap<>();
        hibernateProperties.put("hibernate.hbm2ddl.auto",
                this.env.getProperty("hibernate.hbm2ddl.auto"));
        hibernateProperties.put("hibernate.dialect",
                this.env.getProperty("hibernate.dialect"));
        hibernateProperties.put("hibernate.enable_lazy_load_no_trans",
                this.env.getProperty("hibernate.enable_lazy_load_no_trans"));
        hibernateProperties.put("hibernate.proc.param_null_passing",
                String.valueOf(true));
        hibernateProperties.put("hibernate.show_sql",
                env.getProperty("hibernate.show_sql"));
        hibernateProperties.put(org.hibernate.cfg.Environment.MULTI_TENANT,
                MultiTenancyStrategy.SCHEMA);
        TenantConnectionProvider tenantConnectionProvider =
                multiTenantConnectionProvider(hibernateProperties);
        hibernateProperties.put(
                org.hibernate.cfg.Environment.MULTI_TENANT_CONNECTION_PROVIDER,
                tenantConnectionProvider);
        hibernateProperties.put(
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
