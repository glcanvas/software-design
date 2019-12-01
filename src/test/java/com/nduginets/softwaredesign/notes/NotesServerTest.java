package com.nduginets.softwaredesign.notes;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nduginets.softwaredesign.notes.data.NoteDao;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jooq.DSLContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class NotesServerTest {

    private NotesApplication application;
    private NoteDao dao;
    private WebTarget target;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void init() throws ExecutionException, InterruptedException {
        Properties p = new Properties();
        p.setProperty("host", "localhost");
        p.setProperty("port", "8080");
        p.setProperty("jdbc", "jdbc:sqlite:test1.db");

        DSLContext db = DslHolder.createDB(p.getProperty("jdbc"));

        dao = new NoteDao(db);
        dao.createSchema();

        application = new NotesApplication(p);

        CompletableFuture<?> start = application.getHttpServer().start();
        start.get();

        target = ResteasyClientBuilder.newClient()
                .target(String.format("http://%s:%s", p.getProperty("host"), p.getProperty("port")));
    }

    @AfterEach
    public void closeAll() throws ExecutionException, InterruptedException {
        dao.clearSchema();
        CompletableFuture<?> stop = application.getHttpServer().stop();
        application.getHttpServer().close();
        stop.get();
    }

    @Test
    public void noteTest() {
        Response create = createNote(buildNode("a", "aaa", false, null));

        Response get = getNote(1);
        Assertions.assertEquals(201, create.getStatus());
        Assertions.assertEquals(200, get.getStatus());
        JsonNode getNode = get.readEntity(JsonNode.class);
        Assertions.assertEquals(1, getNode.get("id").asInt());
        Assertions.assertEquals("a", getNode.get("name").asText());
        Assertions.assertEquals("aaa", getNode.get("description").asText());
        Assertions.assertEquals("false", getNode.get("done").asText());
        Assertions.assertNull(getNode.get("note-list-id"));
    }

    @Test
    public void noteTestEmpty() {
        Response get = getNote(1);
        Assertions.assertEquals(404, get.getStatus());
    }

    @Test
    public void updateNoteTest() {
        Response create = createNote(buildNode("a", "aaa", false, null));

        Response get = getNote(1);

        Assertions.assertEquals(201, create.getStatus());
        Assertions.assertEquals(200, get.getStatus());

        JsonNode getNode = get.readEntity(JsonNode.class);
        Assertions.assertEquals(1, getNode.get("id").asInt());
        Assertions.assertEquals("a", getNode.get("name").asText());
        Assertions.assertEquals("aaa", getNode.get("description").asText());
        Assertions.assertEquals("false", getNode.get("done").asText());
        Assertions.assertNull(getNode.get("note-list-id"));

        Response update = updateNote(1, buildNode("b", "c", true, null));
        get = getNote(1);
        Assertions.assertEquals(200, update.getStatus());
        Assertions.assertEquals(200, get.getStatus());
        getNode = get.readEntity(JsonNode.class);
        Assertions.assertEquals(1, getNode.get("id").asInt());
        Assertions.assertEquals("b", getNode.get("name").asText());
        Assertions.assertEquals("c", getNode.get("description").asText());
        Assertions.assertEquals("true", getNode.get("done").asText());
        Assertions.assertNull(getNode.get("note-list-id"));
    }

    @Test
    public void listNoteTest() {
        createNote(buildNode("a", "aa", false, null));
        createNote(buildNode("b", "bb", false, null));

        Response listCreate = createNoteList(buildNodeList("l"));
        Assertions.assertEquals(201, listCreate.getStatus());

        Assertions.assertEquals(200,
                updateNote(1, buildNode("a", "aa", false, 1)).getStatus());
        Assertions.assertEquals(200,
                updateNote(2, buildNode("b", "bb", false, 1)).getStatus());

        Response list = getNoteList(1);
        Assertions.assertEquals(200, list.getStatus());
        JsonNode listNode = list.readEntity(JsonNode.class);
        Assertions.assertEquals("l", listNode.get("name").asText());
        Assertions.assertEquals(2, ((ArrayNode) listNode.get("note-list")).size());
    }

    private Response createNoteList(JsonNode node) {
        return target.path("/api/v1/notes/note-list")
                .request()
                .post(Entity.json(node));
    }

    private Response getNoteList(int id) {
        return target.path("/api/v1/notes/note-list")
                .queryParam("id", id)
                .request()
                .get();
    }

    private Response deleteNoteList(int id) {
        return target.path("/api/v1/notes/note-list")
                .queryParam("id", id)
                .request()
                .delete();
    }

    private Response createNote(JsonNode node) {
        return target.path("/api/v1/notes/note")
                .request()
                .post(Entity.json(node));
    }

    private Response getNote(int id) {
        return target.path("/api/v1/notes/note/id")
                .queryParam("id", id)
                .request()
                .get();
    }

    private Response updateNote(int id, JsonNode node) {
        return target.path("/api/v1/notes/note")
                .queryParam("id", id)
                .request()
                .put(Entity.json(node));
    }

    private JsonNode buildNode(String name, String descr, boolean done, Integer listId) {
        ObjectNode node = mapper.createObjectNode();
        node.put("name", name);
        node.put("description", descr);
        node.put("done", done);
        if (listId != null) {
            node.put("note-list-id", listId);
        }
        return node;
    }

    private JsonNode buildNodeList(String name) {
        ObjectNode node = mapper.createObjectNode();
        node.put("name", name);
        return node;
    }
}
