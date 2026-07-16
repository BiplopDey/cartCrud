package com.assignment.cartCrud.respository.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.Properties;

@org.springframework.context.annotation.Configuration
@Profile("sqlite")
public class HibernateSQLiteConfiguration {

    @Bean(destroyMethod = "close")
    SessionFactory sqliteSessionFactory(
            @Value("${cart.sqlite.url:jdbc:sqlite:cartcrud.db}") String databaseUrl) {
        Properties properties = new Properties();
        properties.put(AvailableSettings.DRIVER, "org.sqlite.JDBC");
        properties.put(AvailableSettings.URL, databaseUrl);
        properties.put(AvailableSettings.DIALECT, "org.hibernate.community.dialect.SQLiteDialect");
        properties.put(AvailableSettings.HBM2DDL_AUTO, "update");
        properties.put(AvailableSettings.SHOW_SQL, "false");
        properties.put(AvailableSettings.POOL_SIZE, "1");

        return new Configuration()
                .setProperties(properties)
                .addAnnotatedClass(CartEntity.class)
                .addAnnotatedClass(CartProductEntity.class)
                .buildSessionFactory();
    }
}
