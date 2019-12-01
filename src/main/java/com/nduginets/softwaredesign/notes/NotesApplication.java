package com.nduginets.softwaredesign.notes;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.nduginets.softwaredesign.notes.application.HttpServer;
import com.nduginets.softwaredesign.notes.application.RestApi;
import com.nduginets.softwaredesign.notes.data.NoteDao;
import org.jooq.DSLContext;

import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class NotesApplication {

    public static final String INDEX_TEMPLATE;

    static {
        try {
            INDEX_TEMPLATE = Resources.toString(Resources.getResource("template/index.html"), Charsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        Properties p = new Properties();
        p.setProperty("host", "localhost");
        p.setProperty("port", "8080");
        p.setProperty("jdbc", "jdbc:sqlite:test.db");


        DSLContext db = DslHolder.createDB(p.getProperty("jdbc"));

        NoteDao dao = new NoteDao(db);
        dao.createSchema();

        NotesApplication application = new NotesApplication(p);

        CompletableFuture<?> start = application.getHttpServer().start();

        try {
            start.get();
        } catch (Throwable e) {
            System.err.println("Can't launch server");
            System.exit(-1);
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.next();
            if ("exit".equals(command)) {
                break;
            }
        }
        application.getHttpServer().stop();
        application.getHttpServer().close();
    }


    private final HttpServer httpServer;

    public NotesApplication(Properties properties) {
        DSLContext db = DslHolder.createDB(properties.getProperty("jdbc"));
        RestApi api = new NotesApi(new NotesServer(db));
        this.httpServer = new HttpServer(properties, api);
    }

    public HttpServer getHttpServer() {
        return httpServer;
    }
}
