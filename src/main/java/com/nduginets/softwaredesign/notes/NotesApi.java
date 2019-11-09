package com.nduginets.softwaredesign.notes;

import com.nduginets.softwaredesign.notes.application.RestApi;
import com.nduginets.softwaredesign.notes.data.Note;
import com.nduginets.softwaredesign.notes.data.NoteList;
import javafx.util.Pair;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/v1/notes")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class NotesApi implements RestApi {

    private final NotesServer server;

    public NotesApi(NotesServer server) {
        this.server = server;
    }

    @GET
    @Path("/note/id")
    public Response getNotes(@QueryParam("id") @NotNull Integer id) {
        Pair<Response.Status, Note> res = server.getNote(id);
        return Response.status(res.getKey()).entity(res.getValue()).build();
    }

    @GET
    @Path("/note/all")
    public Response getAllNotes() {
        return Response.ok(server.getNotes()).build();
    }

    @POST
    @Path("/note")
    public Response postNote(@Valid Note note) {
        server.createNote(note);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/note")
    public Response modifyNote(@QueryParam("id") @NotNull Integer id, @NotNull Note note) {
        Response.Status res = server.updateNode(id, note);
        return Response.status(res).entity(res).build();
    }


    @GET
    @Path("/note-list")
    public Response getNoteList(@QueryParam("id") @NotNull Integer id) {
        Pair<Response.Status, NoteList> res = server.getNoteList(id);
        return Response.status(res.getKey()).entity(res.getValue()).build();
    }

    @POST
    @Path("/note-list")
    public Response postNoteList(@Valid NoteList noteList) {
        server.createNoteList(noteList);
        return Response.status(Response.Status.CREATED).build();
    }


    @DELETE
    @Path("/note-list")
    public Response deleteNoteList(@QueryParam("id") @NotNull Integer id) {
        server.deleteNoteList(id);
        return Response.ok().build();
    }
}
