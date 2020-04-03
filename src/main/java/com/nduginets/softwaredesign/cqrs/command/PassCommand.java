package com.nduginets.softwaredesign.cqrs.command;

import java.time.Instant;
import java.util.Objects;

public class PassCommand extends Command {

    private final PassType passType;

    public PassCommand(int userId, Instant actionTime, PassType passType) {
        super(userId, actionTime);
        this.passType = passType;
    }

    public PassType getPassType() {
        return passType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PassCommand that = (PassCommand) o;
        return passType == that.passType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), passType);
    }

    @Override
    public String toString() {
        return super.toString() + "PassCommand{" +
                "passType=" + passType +
                '}';
    }

    public enum PassType {
        ENTER,
        EXIT;
    }
}
