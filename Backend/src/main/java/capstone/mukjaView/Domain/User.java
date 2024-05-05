package capstone.mukjaView.Domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column
    private String username;

    @Column(nullable = false)
    private String name;

    @Column
    private String email;

    @Column
    private String neutralPicture;

    @Column
    private String smilePicture;

    @Column
    private String sadPicture;

    @Column
    private String mukbti;

    @Column
    private boolean init;

    @Column
    private String role;

    public User() {
        init = false;
    }
}
