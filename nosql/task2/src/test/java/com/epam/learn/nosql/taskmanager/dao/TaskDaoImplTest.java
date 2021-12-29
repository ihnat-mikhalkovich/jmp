package com.epam.learn.nosql.taskmanager.dao;

import com.epam.learn.nosql.taskmanager.entity.Subtask;
import com.epam.learn.nosql.taskmanager.entity.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.bson.Document;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class TaskDaoImplTest {

    private static final String COLLECTION_NAME = "tasks";
    private static final String DATABASE_NAME = "junit";

    static MongoClient mongoClient;
    static MongoCollection<Document> mongoCollection;
    static MongodProcess mongod;

    static {
        System.setProperty("os.arch", "i686_64");
    }

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final TaskDaoImpl tasksDao = new TaskDaoImpl(mongoCollection, objectMapper);

    {
        tasksDao.setNowDateSupplier(() -> ZonedDateTime.parse("2021-06-17T18:00:00+03:00"));
    }

    @BeforeAll
    static void prepareMongoClient() throws IOException {
        final MongodStarter starter = MongodStarter.getDefaultInstance();

        final int port = Network.getFreeServerPort();
        final MongodConfig mongodConfig = MongodConfig.builder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(port, Network.localhostIsIPv6()))
                .build();

        final MongodExecutable mongodExecutable = starter.prepare(mongodConfig);
        mongod = mongodExecutable.start();
        mongoClient = MongoClients.create("mongodb://localhost:" + port);
        mongoCollection = mongoClient
                .getDatabase(DATABASE_NAME)
                .getCollection(COLLECTION_NAME);
    }

    @AfterAll
    static void closeMongoClient() {
        mongod.stop();
        mongoClient.close();
    }


    @BeforeEach
    void setupCollection() throws Exception {
        MongoDatabase db = mongoClient.getDatabase(DATABASE_NAME);

        final MongoCollection<Document> tasks = db.getCollection(COLLECTION_NAME);
        final String json = Files.lines(Paths.get("./src/main/resources/data.json"))
                .reduce((s1, s2) -> s1 + s2)
                .get();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        final Task[] taskArray = objectMapper.readValue(json, Task[].class);

        for (Task task : taskArray) {
            final String json1 = objectMapper.writeValueAsString(task);
            tasks.insertOne(Document.parse(json1));
        }
    }

    @AfterEach
    void tearDown() {
        mongoClient.getDatabase(DATABASE_NAME).getCollection(COLLECTION_NAME).drop();
    }

    @Test
    public void findAll() throws IOException {
        // given
        final List<Task> expected = this.getTasksFromFile(Paths.get("./src/main/resources/findAllResult.json"));
        // when
        final List<Task> result = tasksDao.findAll();
        // then
        result.forEach(task -> task.setId(null));
        assertIterableEquals(expected, result);
    }

    @Test
    public void overdueTasks() throws IOException {
        // given
        final List<Task> expected = this.getTasksFromFile(Paths.get("./src/main/resources/overdueTasksResult.json"));
        // when
        final List<Task> result = tasksDao.overdueTasks();
        // then
        result.forEach(task -> {
            task.setId(null);
        });
        assertIterableEquals(expected, result);
    }

    @Test
    public void findByCategory() throws IOException {
        // given
        final List<Task> expected = this.getTasksFromFile(Paths.get("./src/main/resources/findByCategory.json"));
        // when
        final List<Task> result = tasksDao.findAll("cassandra");
        // then
        result.forEach(task -> task.setId(null));
        assertIterableEquals(expected, result);
    }

    @Test
    public void createTask() throws IOException {
        // given
        final Task tasksToCreate = this.getTaskFromFile(Paths.get("./src/main/resources/createTask.json"));
        final List<Task> beforeTaskCreation = this.tasksDao.findAll();
        // when
        final String taskId = tasksDao.createTask(tasksToCreate);
        // then
        final List<Task> afterTaskCreation = this.tasksDao.findAll();

        assertTrue(afterTaskCreation.removeAll(beforeTaskCreation));
        assertFalse(afterTaskCreation.isEmpty());
        final Task result = afterTaskCreation.iterator().next();
        assertEquals(taskId, result.getId());
        result.setId(null);
        assertEquals(tasksToCreate, result);
    }

    @Test
    public void deleteTask() throws IOException {
        // given
        final Task tasksToCreate = this.getTaskFromFile(Paths.get("./src/main/resources/deleteTask.json"));
        final String idFromFile = tasksToCreate.getId();
        final String createdTaskId = tasksDao.createTask(tasksToCreate);
        // when
        tasksDao.deleteTask(createdTaskId);
        // then
        assertAll(
                () -> assertTrue(tasksDao.findAll().stream()
                        .noneMatch(task -> createdTaskId.equals(task.getId()))
                ),
                () -> assertEquals(idFromFile, createdTaskId)
        );
    }

    @Test
    public void updateTask() throws IOException {
        // given
        final Task task = this.getTaskFromFile(Paths.get("./src/main/resources/createTask.json"));
        final String taskId = tasksDao.createTask(task);

        final Task updateTask = new Task();
        updateTask.setId(taskId);
        updateTask.setName("New name");
        // when
        final Task updatedTask = tasksDao.updateTask(updateTask);
        // then
        assertEquals(updatedTask.getId(), updatedTask.getId());
        assertEquals(updatedTask.getName(), "New name");
        final Optional<Task> first = tasksDao.findAll().stream()
                .filter(t -> taskId.equals(t.getId()))
                .findFirst();
        assertTrue(first.isPresent());
        assertEquals("New name", first.get().getName());
    }

    @Test
    public void createSubtasks() {
        // given
        final List<Subtask> subtasks = Arrays.asList(
                new Subtask("new-subtask-name-1", "new-subtask-description-1"),
                new Subtask("new-subtask-name-2", "new-subtask-description-2")
        );
        final Task first = tasksDao.findAll().iterator().next();
        final Task taskWithIdAndNewSubtasks = new Task();
        taskWithIdAndNewSubtasks.setId(first.getId());
        taskWithIdAndNewSubtasks.setSubtasks(subtasks);
        final List<Subtask> expected = new ArrayList<>(first.getSubtasks());
        expected.addAll(taskWithIdAndNewSubtasks.getSubtasks());
        // when
        final List<Subtask> result = tasksDao.createSubtasks(taskWithIdAndNewSubtasks);
        // then
        assertIterableEquals(expected, result);
    }

    @Test
    public void deleteSubtasks() {
        // given
        final Task first = tasksDao.findAll().iterator().next();
        final Task task = new Task();
        task.setId(first.getId());
        final List<Subtask> subtasksToDelete = first.getSubtasks().stream().limit(2).collect(Collectors.toList());
        task.setSubtasks(subtasksToDelete);
        final List<Subtask> expected = new ArrayList<>(first.getSubtasks());
        expected.removeAll(task.getSubtasks());
        // when
        final List<Subtask> result = tasksDao.deleteSubtasks(task);
        // then
        assertIterableEquals(expected, result);
    }

    @Test
    public void fullTextSearch() throws IOException {
        // given
        mongoClient
                .getDatabase(DATABASE_NAME)
                .getCollection(COLLECTION_NAME)
                .createIndex(new Document()
                        .append("description", "text")
                        .append("$**", "text")
                );

        final List<Task> expected = this.getTasksFromFile(Paths.get("./src/main/resources/fullTextSearch.json"));
        final String textToSearch = "driver";
        //when
        final List<Task> result = tasksDao.fullTextSearch(textToSearch);
        // then
        result.forEach(task -> task.setId(null));
        assertThat(result)
                .hasSameElementsAs(expected);
    }

    private Object getFromFile(Path resourcePath, Class<?> clazz) throws IOException {
        final String json = Files.lines(resourcePath)
                .reduce((s1, s2) -> s1 + s2)
                .get();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper.readValue(json, clazz);
    }

    private List<Task> getTasksFromFile(Path resourcePath) throws IOException {
        final Task[] taskArray = (Task[]) this.getFromFile(resourcePath, Task[].class);
        return Arrays.asList(taskArray);
    }

    private Task getTaskFromFile(Path resourcePath) throws IOException {
        return (Task) this.getFromFile(resourcePath, Task.class);
    }
}