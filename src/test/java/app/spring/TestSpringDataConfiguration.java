package app.spring;

import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScans(value = {
        @ComponentScan("app.dao"),
        @ComponentScan("app.models"),
        @ComponentScan("app.service"),
        @ComponentScan("app.configuration.spring.constants")
})
public class TestSpringDataConfiguration {

    @Bean
    public DataSource getDataSource() {
        String dbDriver = "com.mysql.jdbc.Driver";
        String dbPassword = "1324domik";
        String dbUrl = "jdbc:mysql://192.168.31.76/bcrew?useSSL=false&serverTimezone=UTC";
        String dbUsername = "bcrew_user";
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(dbDriver);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(getDataSource());
        em.setPackagesToScan(new String[]{"app.models"});
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(hibernateProperties());

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    private final Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();
        String hiberAuto = "none";
        String hiberNoTrans = "true";
        String hiberDialect = "org.hibernate.dialect.MySQL5Dialect";
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto",
                hiberAuto);
        hibernateProperties.setProperty("hibernate.dialect", hiberDialect);
        hibernateProperties.setProperty("hibernate.enable_lazy_load_no_trans",
                hiberNoTrans);
        return hibernateProperties;
    }
}
