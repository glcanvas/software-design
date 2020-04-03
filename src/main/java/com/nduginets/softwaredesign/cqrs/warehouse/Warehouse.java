package com.nduginets.softwaredesign.cqrs.warehouse;

import org.jooq.DSLContext;
import org.jooq.impl.SQLDataType;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.e;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class Warehouse {

    private DSLContext db;

    public Warehouse(DSLContext db) {
        this.db = db;
        createSchema();
    }

    private void createSchema() {
        UserEvent.createUserEventSchema(db);
        PassEvent.createPassEventSchema(db);
    }

    public void dropDB() {
        db.delete(table("pass_event")).execute();
        db.delete(table("user_event")).execute();
    }

    public List<PassEvent> getPassEvents() {
        return db.select(
                field("action_type", SQLDataType.BOOLEAN),
                field("action_time", SQLDataType.BIGINT),
                field("user_id", SQLDataType.INTEGER))
                .from("pass_event")
                .fetch()
                .stream()
                .map(x -> new PassEvent(x.component1(), x.component2(), x.component3()))
                .collect(Collectors.toList());
    }

    public List<PassEvent> getPassEventsForUser(int userId) {
        return db.select(
                field("action_type", SQLDataType.BOOLEAN),
                field("action_time", SQLDataType.BIGINT),
                field("user_id", SQLDataType.INTEGER))
                .from("pass_event")
                .where(field("user_id", SQLDataType.INTEGER).eq(userId))
                .fetch()
                .stream()
                .map(x -> new PassEvent(x.component1(), x.component2(), x.component3()))
                .collect(Collectors.toList());
    }

    public List<PassEvent> getPassEventsFrom(Instant from) {
        long fromEpochMilli = from.toEpochMilli();
        return db.select(
                field("action_type", SQLDataType.BOOLEAN),
                field("action_time", SQLDataType.BIGINT),
                field("user_id", SQLDataType.INTEGER))
                .from("pass_event")
                .where(field("action_time", SQLDataType.BIGINT).ge(fromEpochMilli))
                .fetch()
                .stream()
                .map(x -> new PassEvent(x.component1(), x.component2(), x.component3()))
                .collect(Collectors.toList());
    }

    public List<UserEvent> getUserEvents() {
        return db.select(
                field("time_begin", SQLDataType.DATE),
                field("time_end", SQLDataType.DATE),
                field("action_time", SQLDataType.BIGINT),
                field("user_id", SQLDataType.INTEGER))
                .from("user_event")
                .fetch()
                .stream()
                .map(x -> new UserEvent(x.component1(), x.component2(), x.component3(), x.component4()))
                .collect(Collectors.toList());
    }

    public List<UserEvent> getUserEventsFrom(Instant from) {
        long fromEpochMilli = from.toEpochMilli();
        return db.select(
                field("time_begin", SQLDataType.DATE),
                field("time_end", SQLDataType.DATE),
                field("action_time", SQLDataType.BIGINT),
                field("user_id", SQLDataType.INTEGER))
                .from("user_event")
                .where(field("action_time", SQLDataType.BIGINT).ge(fromEpochMilli))
                .fetch()
                .stream()
                .map(x -> new UserEvent(x.component1(), x.component2(), x.component3(), x.component4()))
                .collect(Collectors.toList());
    }

    public List<UserEvent> getUserEventsForUser(int userId) {
        return db.select(
                field("time_begin", SQLDataType.DATE),
                field("time_end", SQLDataType.DATE),
                field("action_time", SQLDataType.BIGINT),
                field("user_id", SQLDataType.INTEGER))
                .from("user_event")
                .where(field("user_id", SQLDataType.INTEGER).eq(userId))
                .fetch()
                .stream()
                .map(x -> new UserEvent(x.component1(), x.component2(), x.component3(), x.component4()))
                .collect(Collectors.toList());
    }

    public void insertUserEvent(UserEvent event) {
        db.insertInto(table("user_event"),
                field("time_begin", SQLDataType.DATE),
                field("time_end", SQLDataType.DATE),
                field("action_time", SQLDataType.BIGINT),
                field("user_id", SQLDataType.INTEGER))
                .values(event.getTimeBegin(), event.getTimeEng(),
                        event.getActionTime(), event.getUserId())
                .execute();
    }

    public void insertPassEvent(PassEvent event) {
        db.insertInto(table("pass_event"),
                field("action_type", SQLDataType.BOOLEAN),
                field("action_time", SQLDataType.BIGINT),
                field("user_id", SQLDataType.INTEGER))
                .values(event.isActionType(), event.getActionTime(), event.getUserId())
                .execute();
    }

}
