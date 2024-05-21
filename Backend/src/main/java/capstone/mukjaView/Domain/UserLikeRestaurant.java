package capstone.mukjaView.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class UserLikeRestaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userRestaurantId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurant;

}
