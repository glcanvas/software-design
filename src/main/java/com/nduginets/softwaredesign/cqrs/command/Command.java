package com.nduginets.softwaredesign.cqrs.command;

import java.time.Instant;
import java.util.Objects;

public class Command {
    protected final int userId;
    protected final Instant actionTime;

    public Command(int userId, Instant actionTime) {
        this.userId = userId;
        this.actionTime = actionTime;
    }

    public int getUserId() {
        return userId;
    }

    public Instant getActionTime() {
        return actionTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return userId == command.userId &&
                Objects.equals(actionTime, command.actionTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, actionTime);
    }

    @Override
    public String toString() {
        return "Command{" +
                "userId=" + userId +
                ", actionTime=" + actionTime +
                '}';
    }
}
