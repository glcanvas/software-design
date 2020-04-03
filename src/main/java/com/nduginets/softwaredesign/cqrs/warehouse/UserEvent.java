package com.nduginets.softwaredesign.cqrs.warehouse;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

public class UserEvent implements Event {

    private final Date timeBegin;
    private final Date timeEng;
    private final long actionTime;
    private final int userId;

    public UserEvent(Date timeBegin, Date timeEng, long actionTime, int userId) {
        this.timeBegin = timeBegin;
        this.timeEng = timeEng;
        this.actionTime = actionTime;
        this.userId = userId;
    }

    public Date getTimeBegin() {
        return timeBegin;
    }

    public Date getTimeEng() {
        return timeEng;
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
        UserEvent userEvent = (UserEvent) o;
        return userId == userEvent.userId &&
                Objects.equals(timeBegin, userEvent.timeBegin) &&
                Objects.equals(timeEng, userEvent.timeEng) &&
                Objects.equals(actionTime, userEvent.actionTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeBegin, timeEng, actionTime, userId);
    }

    @Override
    public String toString() {
        return "UserEvent{" +
                "timeBegin=" + timeBegin +
                ", timeEng=" + timeEng +
                ", actionTime=" + actionTime +
                ", userId=" + userId +
                '}';
    }

    public static void createUserEventSchema(DSLContext context) {
        context.createTableIfNotExists("user_event")
                .column("time_begin", SQLDataType.DATE)
                .column("time_end", SQLDataType.DATE)
                .column("action_time", SQLDataType.BIGINT) // Instant
                .column("user_id", SQLDataType.INTEGER)
                .constraint(DSL.constraint("user_event_unique")
                        .primaryKey("user_id", "action_time"))
        .execute();
    }
}
