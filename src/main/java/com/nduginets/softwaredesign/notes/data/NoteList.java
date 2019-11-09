package com.nduginets.softwaredesign.notes.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoteList {

    private String name;
    private int noteListId;
    private List<Note> noteList;

    public NoteList() {
    }

    public NoteList(int noteListId, String name, List<Note> notes) {
        this.name = name;
        this.noteListId = noteListId;
        this.noteList = notes;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty(value = "note-list-id", access = JsonProperty.Access.READ_ONLY)
    public int getNoteListId() {
        return noteListId;
    }

    public void setNoteListId(int noteListId) {
        this.noteListId = noteListId;
    }

    @JsonProperty(value = "note-list", access = JsonProperty.Access.READ_ONLY)
    public List<Note> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteList noteList = (NoteList) o;
        return noteListId == noteList.noteListId &&
                Objects.equals(name, noteList.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, noteListId);
    }

    @Override
    public String toString() {
        return "NoteList{" +
                "name='" + name + '\'' +
                ", noteListId=" + noteListId +
                '}';
    }

    public static void createNodeList(DSLContext context) {
        context.createTableIfNotExists("note_list")
                .column("note_list_id", SQLDataType.INTEGER)
                .column("name", SQLDataType.VARCHAR.length(20))
                .constraint(DSL.constraint("pk_note_list").primaryKey("note_list_id"))
                .execute();
    }
}
