package com.nduginets.softwaredesign.notes;

import com.nduginets.softwaredesign.notes.application.RestApi;
import com.nduginets.softwaredesign.notes.data.Note;
import com.nduginets.softwaredesign.notes.data.NoteDao;
import com.nduginets.softwaredesign.notes.data.NoteList;
import javafx.util.Pair;
import org.jooq.DSLContext;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

public class NotesServer {

    private final NoteDao dao;

    public NotesServer(DSLContext db) {
        this.dao = new NoteDao(db);
    }

    public void createNote(Note note) {
        dao.addNote(note);
    }

    public Response.Status updateNode(int id, Note note) {
        boolean result = dao.updateNote(id, note);
        if (result) {
            return Response.Status.OK;
        }
        return Response.Status.NOT_FOUND;
    }

    public Pair<Response.Status, Note> getNote(int id) {
        Optional<Note> result = dao.getNote(id);
        return result
                .map(note -> new Pair<>(Response.Status.OK, note))
                .orElseGet(() -> new Pair<>(Response.Status.NOT_FOUND, null));
    }

    public List<Note> getNotes() {
        return dao.getNotes();
    }


    public void createNoteList(NoteList noteList) {
        dao.addNoteList(noteList);
    }

    public Pair<Response.Status, NoteList> getNoteList(int idNoteList) {
        Optional<NoteList> result = dao.getNoteList(idNoteList);
        return result
                .map(note -> new Pair<>(Response.Status.OK, note))
                .orElseGet(() -> new Pair<>(Response.Status.NOT_FOUND, null));
    }

    public void deleteNoteList(int idNoteList) {
        dao.deleteNodeList(idNoteList);
    }

}
