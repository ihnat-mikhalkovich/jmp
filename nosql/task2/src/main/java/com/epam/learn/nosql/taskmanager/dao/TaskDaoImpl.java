package com.epam.learn.nosql.taskmanager.dao;

import com.epam.learn.nosql.taskmanager.entity.Subtask;
import com.epam.learn.nosql.taskmanager.entity.Task;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
@RequiredArgsConstructor
public class TaskDaoImpl implements TaskDao, NowDateSupplier {
    private final MongoCollection<Document> mongoCollection;
    private final ObjectMapper objectMapper;

    /**
     * When the profile is 'test' then
     * {@link com.epam.learn.nosql.taskmanager.config.NowDateSupplierBeanPostProcessor}
     * has used to set the constant return value.
     */
    @Setter
    @Getter
    private Supplier<ZonedDateTime> nowDateSupplier = ZonedDateTime::now;

    @Override
    public List<Task> findAll() {
        final FindIterable<Document> taskDocuments = mongoCollection.find();
        return toTaskList(taskDocuments);
    }

    private List<Task> toTaskList(MongoIterable<Document> taskDocuments) {
        return StreamSupport.stream(taskDocuments.spliterator(), false)
                .map(this::toTask)
                .collect(Collectors.toList());
    }

    private Task toTask(Document taskDocument) {
        return objectMapper.convertValue(taskDocument, Task.class);
    }

    @Override
    public List<Task> overdueTasks() {
        final List<Document> pipeline = new ArrayList<>();
        pipeline.add(new Document().append("$addFields", new Document()
                .append("isoDeadline", new Document()
                        .append("$toDate", "$deadline"))));

        pipeline.add(new Document().append("$match", new Document()
                .append("isoDeadline", new Document()
                        .append("$lt", Date.from(this.nowDateSupplier.get().toInstant())))));

        pipeline.add(new Document().append("$project", new Document()
                .append("_id", 1)
                .append("dateOfCreation", 1)
                .append("deadline", 1)
                .append("name", 1)
                .append("description", 1)
                .append("subtasks", 1)
                .append("category", 1)));

        final AggregateIterable<Document> aggregate = mongoCollection.aggregate(pipeline);
        return toTaskList(aggregate);
    }

    @Override
    public List<Task> findAll(String category) {
        final FindIterable<Document> taskDocuments = mongoCollection.find(Filters.eq("category", category));
        return toTaskList(taskDocuments);
    }

    @Override
    public String createTask(Task task) {
        final Document taskDocument = objectMapper.convertValue(task, Document.class);

        if (taskDocument.containsKey("_id")) {
            taskDocument.put("_id", new ObjectId(task.getId()));
        }

        mongoCollection.insertOne(taskDocument);
        return taskDocument.get("_id").toString();
    }

    @Override
    public void deleteTask(String id) {
        final Document deleteDocument = new Document().append("_id", new ObjectId(id));
        mongoCollection.deleteOne(deleteDocument);
    }

    @Override
    public Task updateTask(Task task) {
        final Document taskDocument = objectMapper.convertValue(task, Document.class);
        taskDocument.remove("_id");

        final Document filter = new Document().append("_id", new ObjectId(task.getId()));
        final Document update = new Document().append("$set", taskDocument);

        mongoCollection.updateOne(filter, update);

        return toTask(mongoCollection.find(filter).first());
    }

    @Override
    public List<Subtask> createSubtasks(Task task) {
        final TypeReference<List<Document>> documentListType = new TypeReference<List<Document>>() {
        };
        final List<Document> taskDocument = objectMapper.convertValue(task.getSubtasks(), documentListType);

        final Document filter = new Document().append("_id", new ObjectId(task.getId()));
        final Document update = new Document().append("$push", new Document()
                .append("subtasks", new Document()
                        .append("$each", taskDocument)));

        mongoCollection.updateOne(filter, update);

        return toTask(mongoCollection.find(filter).first()).getSubtasks();
    }

    @Override
    public List<Subtask> deleteSubtasks(Task task) {
        final TypeReference<List<Document>> documentListType = new TypeReference<List<Document>>() {
        };
        final List<Document> taskDocument = objectMapper.convertValue(task.getSubtasks(), documentListType);

        final Document filter = new Document().append("_id", new ObjectId(task.getId()));
        final Document update = new Document().append("$pull", new Document()
                .append("subtasks", new Document()
                        .append("$in", taskDocument)));

        mongoCollection.updateOne(filter, update);

        return toTask(mongoCollection.find(filter).first()).getSubtasks();
    }

    @Override
    public List<Task> fullTextSearch(String text) {
        final FindIterable<Document> documents = mongoCollection.find(
                new Document()
                        .append("$text", new Document()
                                .append("$search", text)));

        return toTaskList(documents);
    }
}