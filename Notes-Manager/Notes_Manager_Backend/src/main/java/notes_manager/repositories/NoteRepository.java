package notes_manager.repositories;

import notes_manager.entities.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    boolean existsNoteById(Long id);

    Optional<Note> findById(Long id);

    List<Note> findAll();

    Note save(Note n);

    void deleteById(Long id);

    List<Note> findByArchived(Boolean archived);
}
