package com.nduginets.softwaredesign.cqrs.command;

public interface CommandProcessor<T extends Command> {
    void execute(T command);
}
