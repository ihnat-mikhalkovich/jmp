package com.ihnat.mikhalkovich.guestservices;

import com.ihnat.mikhalkovich.guestservices.entity.Guest;
import com.ihnat.mikhalkovich.guestservices.repository.GuestRepository;
import com.mongodb.MongoClient;
import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Indexes;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@SpringBootApplication
public class GuestServicesApplication {

    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(GuestServicesApplication.class, args);

        final GuestRepository guestRepository = context.getBean(GuestRepository.class);
        final List<Guest> guests = StreamSupport.stream(guestRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        migrateFromSqlToMongodbJobWithResultingList(guests);

        context.close();
    }

    public static void migrateFromSqlToMongodbJobWithResultingList(List<Guest> guests) {
        // connect to mongo replica set
        final MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://mongo1:9042,mongo2:9142,mongo3:9242/?replicaSet=rs0"));
        final MongoDatabase test = mongoClient.getDatabase("test");

        // start transaction
        final ClientSession clientSession = mongoClient.startSession();

        final TransactionOptions txnOptions = TransactionOptions.builder()
                .readPreference(ReadPreference.primary())
                .readConcern(ReadConcern.LOCAL)
                .writeConcern(WriteConcern.MAJORITY)
                .build();

        final TransactionBody<String> txnBody = () -> {
            // drop old collection
            final MongoCollection<Document> mongoGuest = test.getCollection("mongoGuest");
            mongoGuest.drop();

            // create new collection
            test.createCollection("mongoGuest");
            final MongoCollection<Document> newMongoGuest = test.getCollection("mongoGuest");

            // fulfill new collection
            final List<Document> guestDocuments = guests.stream()
                    .map(mg -> new Document()
                            .append("_id", mg.getId())
                            .append("firstName", mg.getFirstName())
                            .append("lastName", mg.getLastName())
                            .append("emailAddress", mg.getEmailAddress())
                            .append("address", mg.getAddress())
                            .append("country", mg.getCountry())
                            .append("state", mg.getState())
                            .append("phoneNumber", mg.getPhoneNumber()))
                    .collect(Collectors.toList());

            newMongoGuest.insertMany(guestDocuments);
            return "Drop old collections. Create new collection. FulFill new collection.";
        };

        // start transaction. try/finally to avoid abandoned lock
        try {
            clientSession.withTransaction(txnBody, txnOptions);
        } finally {
            clientSession.close();
        }
        
        // create index on country field
        test.getCollection("mongoGuest").createIndex(Indexes.ascending("country"));

        // create index on state field
        test.getCollection("mongoGuest").createIndex(Indexes.ascending("state"));

        // get list with aggregation
        final List<Document> pipeline = new ArrayList<>();
        pipeline.add(new Document().append("$match", new Document()
                .append("country", "United States")));
        pipeline.add(new Document()
                .append("$group", new Document()
                        .append("_id", "$state")
                        .append("total_count", new Document()
                                .append("$sum", 1))));
        pipeline.add(new Document()
                .append("$sort", new Document()
                        .append("_id", 1)));

        final AggregateIterable<Document> aggregate = test.getCollection("mongoGuest").aggregate(pipeline);
        StreamSupport.stream(aggregate.spliterator(), false)
                .forEach(item -> log.info(String.valueOf(item)));
    }
}
