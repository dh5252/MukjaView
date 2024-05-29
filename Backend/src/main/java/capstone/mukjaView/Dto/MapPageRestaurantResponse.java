package capstone.mukjaView.Dto;

import capstone.mukjaView.Domain.Restaurant;
import capstone.mukjaView.Domain.User;
import capstone.mukjaView.Service.RestaurantService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MapPageRestaurantResponse {

    private Long restaurantId;
    private String restaurantName;
    private List<String> tags;
    private String address;
    private double latitude;
    private double longitude;
    private String thumbnailPictureUrl;
    private String emotion;
    private double score;


    public MapPageRestaurantResponse(Restaurant restaurant, User user) {
        this.restaurantId = restaurant.getRestaurantId();
        this.restaurantName = restaurant.getRestaurantName();
        this.address = restaurant.getAddress();
        this.latitude = restaurant.getLatitude();
        this.longitude = restaurant.getLongitude();
        this.thumbnailPictureUrl = restaurant.getThumbnailPicture();
        this.tags = restaurant.getTags().stream()
                .map(o -> o.getTag().toString())
                .collect(Collectors.toList());
        this.score = 0;
        this.emotion = "neutral";
    }
}
