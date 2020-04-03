package com.nduginets.softwaredesign.cqrs.executor;

import com.nduginets.softwaredesign.cqrs.command.Command;
import com.nduginets.softwaredesign.cqrs.command.UserCommand;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class UserCommandExecutor implements CommandExecutor<UserCommand> {

    @Override
    public UserCommand fetchToLastState(List<UserCommand> commands) {


        if (commands == null || commands.isEmpty()) {
            return null;
        }
        commands.sort(Comparator.comparing(Command::getActionTime).thenComparing(Command::getUserId));

        LocalDate lastBegin = commands.get(0).getTimeBegin();
        LocalDate lastEnd = commands.get(0).getTimeEnd();

        for (UserCommand currentCommand : commands) {
            if (!currentCommand.getTimeBegin().isEqual(lastBegin)) {
                lastBegin = currentCommand.getTimeBegin();
            }

            if (!currentCommand.getTimeEnd().isEqual(lastEnd)) {
                lastEnd = currentCommand.getTimeEnd();
            }
        }
        return new UserCommand(commands.get(0).getUserId(), commands.get(commands.size() - 1).getActionTime(),
                lastBegin, lastEnd);
    }
}
