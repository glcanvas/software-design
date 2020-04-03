package com.nduginets.softwaredesign.cqrs.mapper;

import com.nduginets.softwaredesign.cqrs.command.Command;
import com.nduginets.softwaredesign.cqrs.warehouse.Event;

public interface EventMapper<V extends Command, U extends Event> {

    V map(U u);

    U map(V v);
}
