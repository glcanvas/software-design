package com.nduginets.softwaredesign.cqrs.executor;


import com.nduginets.softwaredesign.cqrs.command.Command;
import com.nduginets.softwaredesign.cqrs.command.PassCommand;

import java.util.Comparator;
import java.util.List;

public class PassCommandExecutor implements CommandExecutor<PassCommand> {

    @Override
    public PassCommand fetchToLastState(List<PassCommand> commands) {
        if (commands == null || commands.isEmpty()) {
            return null;
        }
        commands.sort(Comparator.comparing(Command::getActionTime).thenComparing(Command::getUserId));
        return commands.get(commands.size() - 1);
    }
}
