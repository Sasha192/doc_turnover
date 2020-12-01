package app.configuration.spring;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScans({
        @ComponentScan("app.tenantdefault.dao"),
        @ComponentScan("app.tenantdefault.service"),
        @ComponentScan("app.tenantdefault.aspects")
})
public class MongoConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    public MongoClient mongo() {
        String username = environment.getProperty("mongo.credentials.username");
        String pwd = environment.getProperty("mongo.credentials.pwd");
        String host = environment.getProperty("mongo.host");
        String port = environment.getProperty("mongo.port");
        String db = environment.getProperty("mongo.db");
        String authSource = environment.getProperty("mongo.auth_source");
        String uri = String.format(
                "mongodb://%1$s:%2$s@%3$s:%4$s/%5$s",
                username, pwd, host, port, db
        );
        MongoClient mc = new MongoClient(new MongoClientURI(uri));
        return new MongoClient(new MongoClientURI(uri));
    }

    @Bean
    public Datastore getDatasource() {
        Morphia morphia = new Morphia();
        morphia.mapPackage("app.tenantdefault.models");
        Datastore datastore = morphia
                .createDatastore(mongo(), environment.getProperty("mongo.db"));
        datastore.ensureIndexes();
        return datastore;
    }

}
