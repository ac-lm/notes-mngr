package notes_manager.controllers;

import notes_manager.entities.Note;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import notes_manager.services.NoteService;

import java.util.InvalidPropertiesFormatException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class NoteController {

    @Autowired
    private NoteService srv;

    @GetMapping("/notes")
    public ResponseEntity<?> getAllNotes(){
        List<Note> output = srv.getActives();
        if (output != null) {
            return ResponseEntity.ok(output);
        }else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/notes/{id}")
    public ResponseEntity<?> getNoteById(@PathVariable("id") Long id){
        try {
            Note output = srv.getNoteById(id);
            return ResponseEntity.ok(output);
        }catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/notes/archive")
    public ResponseEntity<?> getArchive(){
        try{
            List<Note> output = srv.getArchive();
            return ResponseEntity.ok(output);
        }catch (NullPointerException e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping("/notes/archive/{id}")
    public ResponseEntity<?> archiveNote(@PathVariable Long id){
        if (srv.archive(id))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.internalServerError().build();
    }

    @PatchMapping("/notes/restore/{id}")
    public ResponseEntity<?> restoreNote(@PathVariable Long id){
        if (srv.restore(id))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.internalServerError().build();
    }


    @PostMapping("/notes/new")
    public ResponseEntity<?> createNote(@RequestBody Note note){
        try {
            srv.add(note);
            return ResponseEntity.ok(note);
        }catch (DataIntegrityViolationException e){
            return ResponseEntity.status(409).build();
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/notes/{id}")
    public ResponseEntity<?> updateNote(@PathVariable("id") Long id, @RequestBody Note replacement){
        try{
            Note updated = srv.update(id, replacement);
            return ResponseEntity.ok(updated);
        }catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        } catch (InvalidPropertiesFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/notes/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable("id") Long id){
       try{
           srv.delete(id);
           return ResponseEntity.ok().build();
       }catch (EntityNotFoundException e){
           return ResponseEntity.notFound().build();
       }
    }


}
