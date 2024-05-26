package capstone.mukjaView.Domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column
    private String username;

    @Column
    private String name;

    @Column
    private String nickName;

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

    @OneToMany(mappedBy = "user")
    private List<UserLikeRestaurant> likeRestaurants = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    public User() {
        init = false;
    }
}
