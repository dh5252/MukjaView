package capstone.mukjaView.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantId;

    @Column
    private String address;

    @Column
    private String restaurantName;

    @Column
    private double latitude;

    @Column
    private double longitude;

    @Column
    private String thumbnailPicture;

    @Column
    private double flavorRatio;

    @Column
    private double moodRatio;

    @Column
    private double serviceRatio;

    @Column
    private boolean reasonable;

    @OneToMany(mappedBy = "restaurant")
    private List<RestaurantDetailedPicture> pictures = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<CharacterReview> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<RestaurantTag> tags = new ArrayList<>();
}
