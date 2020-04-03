package com.nduginets.softwaredesign.cqrs.command;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

public class UserCommand extends Command {
    private final LocalDate timeBegin;
    private final LocalDate timeEng;

    public UserCommand(int userId, Instant actionTime, LocalDate timeBegin, LocalDate timeEng) {
        super(userId, actionTime);
        this.timeBegin = timeBegin;
        this.timeEng = timeEng;
    }

    public LocalDate getTimeBegin() {
        return timeBegin;
    }

    public LocalDate getTimeEnd() {
        return timeEng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserCommand that = (UserCommand) o;
        return Objects.equals(timeBegin, that.timeBegin) &&
                Objects.equals(timeEng, that.timeEng);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), timeBegin, timeEng);
    }

    @Override
    public String toString() {
        return "UserCommand{" +
                "timeBegin=" + timeBegin +
                ", timeEng=" + timeEng +
                ", userId=" + userId +
                ", actionTime=" + actionTime +
                '}';
    }
}
