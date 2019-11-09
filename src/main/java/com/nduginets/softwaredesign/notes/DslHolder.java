package com.nduginets.softwaredesign.notes;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

public class DslHolder {

    private static DslHolder holder;

    private final DSLContext dslContext;

    public static synchronized DSLContext createDB(String dbPath) {
        if (holder == null) {
            holder = new DslHolder(dbPath);
        }
        return holder.dslContext;
    }

    private DslHolder(String dbConnection) {
        initJdbc();
        this.dslContext = DSL.using(dbConnection);
    }

    private static void initJdbc() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
