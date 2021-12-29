package com.epam.learn.nosql.taskmanager.config;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoConfig {

    @Value("${db.name}")
    private String databaseName;
    @Value("${db.collection}")
    private String collectionName;
    @Value("${db.url}")
    private String dbUrl;

    @Bean
    public MongoClient mongoClient() {
        if (dbUrl != null) {
            return MongoClients.create(
                    new ConnectionString(dbUrl)
            );
        } else {
            return MongoClients.create();
        }
    }

    @Bean
    public MongoCollection<Document> taskDao(MongoClient mongoClient) {
        return mongoClient
                .getDatabase(databaseName)
                .getCollection(collectionName);
    }
}
