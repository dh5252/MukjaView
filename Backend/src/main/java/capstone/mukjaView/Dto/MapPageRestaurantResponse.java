package capstone.mukjaView.Dto;

import capstone.mukjaView.Domain.Restaurant;
import capstone.mukjaView.Domain.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MapPageRestaurantResponse {

    private long restaurantId;
    private String restaurantName;
    private List<String> tags;
    private String address;
    private double latitude;
    private double longitude;
    private String thumbnailPictureUrl;
    private String emotion;


    public MapPageRestaurantResponse(Restaurant restaurant, User user) {
        this.restaurantId = (long)getRestaurantId();
        this.restaurantName = restaurant.getRestaurantName();
        this.address = restaurant.getAddress();
        this.latitude = restaurant.getLatitude();
        this.longitude = restaurant.getLongitude();
        this.thumbnailPictureUrl = restaurant.getThumbnailPicture();
        this.tags = restaurant.getTags().stream()
                .map(o -> o.getTag().toString())
                .collect(Collectors.toList());

        this.emotion = "neutral";
    }
}
