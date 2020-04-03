package com.nduginets.softwaredesign.cqrs.executor;

import com.nduginets.softwaredesign.cqrs.command.Command;

import java.util.List;

public interface CommandExecutor<T extends Command> {

    T fetchToLastState(List<T> commands);

}
