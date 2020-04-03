package com.nduginets.softwaredesign.cqrs.mapper;

import com.nduginets.softwaredesign.cqrs.command.UserCommand;
import com.nduginets.softwaredesign.cqrs.warehouse.UserEvent;

import java.sql.Date;
import java.time.Instant;

public class UserMapper implements EventMapper<UserCommand, UserEvent> {
    @Override
    public UserCommand map(UserEvent event) {
        return new UserCommand(event.getUserId(), Instant.ofEpochMilli(event.getActionTime()),
                event.getTimeBegin().toLocalDate(), event.getTimeEng().toLocalDate());
    }

    @Override
    public UserEvent map(UserCommand userCommand) {
        return new UserEvent(Date.valueOf(userCommand.getTimeBegin()), Date.valueOf(userCommand.getTimeEnd()),
                userCommand.getActionTime().toEpochMilli(), userCommand.getUserId());
    }
}
