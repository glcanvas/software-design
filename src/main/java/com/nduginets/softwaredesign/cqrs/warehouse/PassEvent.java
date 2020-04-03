package com.nduginets.softwaredesign.cqrs.warehouse;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.time.Instant;
import java.util.Objects;

public class PassEvent implements Event {


    private final boolean actionType;
    private final long actionTime;
    private final int userId;

    public PassEvent(boolean actionType, long actionTime, int userId) {
        this.actionType = actionType;
        this.actionTime = actionTime;
        this.userId = userId;
    }

    public boolean isActionType() {
        return actionType;
    }

    public long getActionTime() {
        return actionTime;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PassEvent passEvent = (PassEvent) o;
        return actionType == passEvent.actionType &&
                userId == passEvent.userId &&
                Objects.equals(actionTime, passEvent.actionTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actionType, actionTime, userId);
    }

    @Override
    public String toString() {
        return "PassEvent{" +
                "actionType=" + actionType +
                ", actionTime=" + actionTime +
                ", userId=" + userId +
                '}';
    }

    public static void createPassEventSchema(DSLContext context) {
        context.createTableIfNotExists("pass_event")
                .column("action_type", SQLDataType.BOOLEAN) // 0 -- enter, 1 -- exit
                .column("action_time", SQLDataType.BIGINT) // Instant
                .column("user_id", SQLDataType.INTEGER)
                .constraint(DSL.constraint("pass_event_unique")
                        .primaryKey("user_id", "action_time"))
        .execute();
    }
}
