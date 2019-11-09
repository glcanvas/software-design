package com.nduginets.softwaredesign.notes.data;

import org.jooq.*;
import org.jooq.impl.SQLDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class NoteDao {

    private DSLContext db;

    public NoteDao(DSLContext db) {
        this.db = db;
    }


    public void createSchema() {
        NoteList.createNodeList(db);
        Note.createNote(db);
    }

    public void clearSchema() {
        db.dropTable("note").execute();
        db.dropTable("note_list").execute();
    }

    public void addNote(Note note) {
        db.insertInto(table("note"), field("note_id", SQLDataType.INTEGER),
                field("name", SQLDataType.VARCHAR.length(20)),
                field("description", SQLDataType.VARCHAR.length(100)),
                field("done", SQLDataType.BOOLEAN.defaulted(false)),
                field("note_list_id", SQLDataType.INTEGER))
                .values(getNoteIndex() + 1, note.getName(), note.getDescription(), note.isDone(), note.getNoteListId())
                .execute();
    }

    public void addNoteList(NoteList noteList) {
        db.insertInto(table("note_list"),
                field("note_list_id", SQLDataType.INTEGER),
                field("name", SQLDataType.VARCHAR.length(20)))
                .values(getNoteListIndex() + 1, noteList.getName())
                .execute();
    }

    public Optional<NoteList> getNoteList(int noteListId) {
        Result<Record5<Integer, String, String, Boolean, Integer>> notes = db.select(
                field("note_id", SQLDataType.INTEGER),
                field("name", SQLDataType.VARCHAR.length(20)),
                field("description", SQLDataType.VARCHAR.length(100)),
                field("done", SQLDataType.BOOLEAN.defaulted(false)),
                field("note_list_id", SQLDataType.INTEGER))
                .from("note")
                .where(field("note_list_id", SQLDataType.INTEGER).eq(noteListId))
                .fetch();
        Result<Record2<Integer, String>> noteList = db.select(
                field("note_list_id", SQLDataType.INTEGER),
                field("name", SQLDataType.VARCHAR.length(20)))
                .from("note_list")
                .where(field("note_list_id", SQLDataType.INTEGER).eq(noteListId))
                .fetch();

        if (noteList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new NoteList(noteList.get(0).value1(), noteList.get(0).value2(), createNotes(notes)));
    }


    public void deleteNodeList(int nodeId) {
        db.deleteFrom(table("note_list"))
                .where(field("name", SQLDataType.INTEGER).eq(nodeId))
                .execute();
        db.update(table("note"))
                .set(field("note_list_id", SQLDataType.INTEGER.nullable(true)), (Integer) null)
                .where(field("note_list_id", SQLDataType.INTEGER).eq(nodeId))
                .execute();
    }


    public boolean updateNote(int id, Note note) {
        int result = db.update(table("note"))
                .set(field("name", SQLDataType.VARCHAR.length(20)), note.getName())
                .set(field("description", SQLDataType.VARCHAR.length(100)), note.getDescription())
                .set(field("done", SQLDataType.BOOLEAN.defaulted(false)), note.isDone())
                .set(field("note_list_id", SQLDataType.INTEGER), note.getNoteListId())
                .where(field("note_id", SQLDataType.INTEGER).eq(id))
                .execute();
        return result > 0;
    }

    public Optional<Note> getNote(int id) {
        Result<Record5<Integer, String, String, Boolean, Integer>> result = db.select(
                field("note_id", SQLDataType.INTEGER),
                field("name", SQLDataType.VARCHAR.length(20)),
                field("description", SQLDataType.VARCHAR.length(100)),
                field("done", SQLDataType.BOOLEAN.defaulted(false)),
                field("note_list_id", SQLDataType.INTEGER))
                .from("note")
                .where(field("note_id", SQLDataType.INTEGER).eq(id))
                .fetch();
        if (result.isEmpty()) {
            return Optional.empty();
        }
        Record5<Integer, String, String, Boolean, Integer> noteRecord = result.get(0);
        return Optional.of(new Note(noteRecord.value1(),
                noteRecord.value2(), noteRecord.value3(), noteRecord.value4(), noteRecord.value5()));
    }

    public List<Note> getNotes() {
        Result<Record5<Integer, String, String, Boolean, Integer>> result = db.select(
                field("note_id", SQLDataType.INTEGER),
                field("name", SQLDataType.VARCHAR.length(20)),
                field("description", SQLDataType.VARCHAR.length(100)),
                field("done", SQLDataType.BOOLEAN.defaulted(false)),
                field("note_list_id", SQLDataType.INTEGER))
                .from("note")
                .fetch();

        return createNotes(result);
    }

    private List<Note> createNotes(Result<Record5<Integer, String, String, Boolean, Integer>> result) {
        List<Note> notes = new ArrayList<>();
        for (Record5<Integer, String, String, Boolean, Integer> r : result) {
            notes.add(new Note(r.value1(), r.value2(), r.value3(), r.value4(), r.value5()));
        }
        return notes;
    }

    private Integer getNoteIndex() {
        Result<Record1<Integer>> result = db.select(field("note_id", SQLDataType.INTEGER).max())
                .from("note")
                .fetch();
        if (result.isEmpty() || result.get(0).value1() == null) {
            return 0;
        }
        return result.get(0).value1();
    }

    private Integer getNoteListIndex() {
        Result<Record1<Integer>> result = db.select(field("note_list_id", SQLDataType.INTEGER).max())
                .from("note_list")
                .fetch();
        if (result.isEmpty() || result.get(0).value1() == null) {
            return 0;
        }
        return result.get(0).value1();
    }
}
