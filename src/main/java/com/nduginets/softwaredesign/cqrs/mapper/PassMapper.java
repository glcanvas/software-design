package com.nduginets.softwaredesign.cqrs.mapper;

import com.nduginets.softwaredesign.cqrs.command.PassCommand;
import com.nduginets.softwaredesign.cqrs.warehouse.PassEvent;

import java.time.Instant;

public class PassMapper implements EventMapper<PassCommand, PassEvent> {
    @Override
    public PassCommand map(PassEvent passEvent) {
        return new PassCommand(passEvent.getUserId(), Instant.ofEpochMilli(passEvent.getActionTime()),
                !passEvent.isActionType() ? PassCommand.PassType.ENTER : PassCommand.PassType.EXIT);
    }

    @Override
    public PassEvent map(PassCommand passCommand) {
        return new PassEvent(passCommand.getPassType() != PassCommand.PassType.ENTER,
                passCommand.getActionTime().toEpochMilli(), passCommand.getUserId());
    }
}
