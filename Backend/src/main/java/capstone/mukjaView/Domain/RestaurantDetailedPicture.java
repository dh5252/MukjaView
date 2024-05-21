package capstone.mukjaView.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class RestaurantDetailedPicture {
    @Id
    private String detailPicture;

    @ManyToOne
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurant;
}
