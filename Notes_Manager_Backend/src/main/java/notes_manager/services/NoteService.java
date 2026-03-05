package notes_manager.services;

import notes_manager.entities.Note;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import notes_manager.repositories.NoteRepository;

import java.time.LocalDateTime;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

@Service
public class NoteService {

    @Autowired
    NoteRepository rep;

    public List<Note> getActives() {
        return rep.findByArchived(false);
    }

    public List<Note> getArchive(){
        return rep.findByArchived(true);
    }

    public boolean archive(Long id) {
        Note toBeArchived = getNoteById(id);
        if (toBeArchived != null && !toBeArchived.isArchived()) {
            toBeArchived.setArchived(true);
            rep.save(toBeArchived);
            return true;
        }
        return false;
    }

    public boolean restore(Long id){
        Note toBeRestored = getNoteById(id);
        if (toBeRestored != null && toBeRestored.isArchived()) {
            toBeRestored.setArchived(false);
            rep.save(toBeRestored);
            return true;
        }
        return false;
    }

    public Note getNoteById(Long id) {
        if (rep.findById(id).isPresent()) {
            return rep.findById(id).get();
        }
        else {
            return null;
        }
    }

    public Note add(Note n) {
        if(n.isEmpty()) throw new RuntimeException("Empty note");

        boolean duplicate = false;
        for (Note note : rep.findAll()) { //Check for duplicates
            if (note.getContent().equals(n.getContent()) && note.getTitle().equals(n.getTitle()) && note.getTag() == n.getTag()) {
                duplicate = true;
                break;
            }
        }
        if (duplicate) {
            throw new DataIntegrityViolationException("Note already exists");
        }
        n.setLastModified(LocalDateTime.now());
        return rep.save(n);
    }


    public Note update(Long id, Note replacement) throws InvalidPropertiesFormatException {
        Note target = getNoteById(id);
        if (target != null) {
            if (!replacement.isEmpty()){
                target.setTitle(replacement.getTitle());
                target.setContent(replacement.getContent());
                target.setLastModified(LocalDateTime.now());
            }
            else  {
                throw new InvalidPropertiesFormatException("Note must have title and content");
            }
            if (replacement.getTag() != null){
                target.setTag(replacement.getTag());
            }
            return rep.save(target);
        }else {
            throw new EntityNotFoundException("Note not found");
        }
    }

    public void delete(Long id) {
        if (rep.existsNoteById(id)) {
            rep.deleteById(id);
        }
        else {
            throw new EntityNotFoundException("Note not found");
        }
    }


}
