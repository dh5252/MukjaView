package capstone.mukjaView.Dto;

import capstone.mukjaView.Domain.Restaurant;
import capstone.mukjaView.Domain.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MapPageRestaurantResponse {

    private String restaurantName;
    private List<String> tags;
    private String address;
    private double latitude;
    private double longitude;
    private String thumbnailPictureUrl;
    private String facePictureUrl;

    public MapPageRestaurantResponse(Restaurant restaurant, User user) {
        // user mukbti 토대로 계산

    }
}
