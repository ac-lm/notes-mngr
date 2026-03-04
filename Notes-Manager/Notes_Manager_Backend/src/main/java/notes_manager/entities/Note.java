package notes_manager.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Setter
@Entity
public class Note {
    private enum Category {
        Family,
        Work,
        Hobbies,
        Other
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String title;

    @Column(length = 140, nullable = false)
    private String content;

    @Column(nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime lastModified;

    @Column
    private Category tag;

    @Column(nullable = false)
    private boolean archived = false;

    public boolean isEmpty() {
        return (title.isBlank() || content.isBlank());
    }

}
