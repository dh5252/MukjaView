package capstone.mukjaView.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RestaurantTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantTagId;

    @ManyToOne
    @JoinColumn(name = "tagName")
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurant;
}
