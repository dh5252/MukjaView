package capstone.mukjaView.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Tag {
    @Id
    private String tagName;

    @OneToMany(mappedBy = "tag")
    private List<RestaurantTag> restaurantTags = new ArrayList<>();
}
