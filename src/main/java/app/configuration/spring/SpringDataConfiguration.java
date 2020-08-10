package app.configuration.spring;

import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource("/WEB-INF/spring/app.properties")
@EnableTransactionManagement
@ComponentScans({
        @ComponentScan("app.dao"),
        @ComponentScan("app.models"),
        @ComponentScan("app.service"),
        @ComponentScan("app.security.models"),
        @ComponentScan("app.security.wrappers"),
        @ComponentScan("app.security.dao"),
        @ComponentScan("app.security.service"),
        @ComponentScan("app.configuration"),
        @ComponentScan("app.utils")
})
public class SpringDataConfiguration {

    @Autowired
    private Environment env;

    @Bean
    public DataSource getDataSource() {
        final BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(this.env.getProperty("db.driver"));
        dataSource.setUrl(this.env.getProperty("db.url"));
        dataSource.setUsername(this.env.getProperty("db.username"));
        dataSource.setPassword(this.env.getProperty("db.password"));
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(this.getDataSource());
        em.setPackagesToScan("app.models", "app.security.models");
        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(this.hibernateProperties());
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

    private final Properties hibernateProperties() {
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
