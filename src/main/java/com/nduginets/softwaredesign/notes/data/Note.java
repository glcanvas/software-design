package com.nduginets.softwaredesign.notes.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.DefaultValue;
import java.util.Objects;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Note {

    private int id;
    private String name;
    private String description;
    private Boolean done;
    private Integer noteListId;

    public Note() {
    }

    public Note(int id, String name, String description, boolean done, Integer noteListId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.done = done;
        this.noteListId = noteListId;
    }

    public Note(String name, String description, boolean done, Integer noteListId) {
        this.name = name;
        this.description = description;
        this.done = done;
        this.noteListId = noteListId;
    }

    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("name")
    @Size(max = 20)
    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("description")
    @Size(max = 100)
    @NotNull
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("done")
    @DefaultValue(value = "false")
    public Boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @JsonProperty("note-list-id")
    public Integer getNoteListId() {
        return noteListId;
    }

    public void setNoteListId(Integer noteListId) {
        this.noteListId = noteListId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return id == note.id &&
                done == note.done &&
                Objects.equals(name, note.name) &&
                Objects.equals(description, note.description) &&
                Objects.equals(noteListId, note.noteListId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, done, noteListId);
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", done=" + done +
                ", noteListId=" + noteListId +
                '}';
    }

    public static void createNote(DSLContext context) {
        context.createTableIfNotExists("note")
                .column("note_id", SQLDataType.INTEGER)
                .column("name", SQLDataType.VARCHAR.length(20))
                .column("description", SQLDataType.VARCHAR.length(100))
                .column("done", SQLDataType.BOOLEAN.defaulted(false))
                .column("note_list_id", SQLDataType.INTEGER.nullable(true))
                .constraint(DSL.constraint("pk_note").primaryKey("note_id"))
                .constraint(DSL.foreignKey("note_list_id")
                        .references("note_list", "note_list_id"))
                .execute();
    }


}
