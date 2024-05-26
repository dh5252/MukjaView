package capstone.mukjaView.Dto;

import capstone.mukjaView.Domain.Restaurant;
import capstone.mukjaView.Domain.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ReviewPageResponse {

    private Long restaurantId;
    private String restaurantName;
    private List<String> tags;
    private List<String> detailedPictureList;
    private String address;
    private String thumbnailPictureUrl;
    private String emotion;
    private double flavorValue;
    private double moodValue;
    private double serviceValue;
    private boolean reasonable;
    private boolean isLike;
    private String fitMukbti;

    public ReviewPageResponse(Restaurant restaurant) {
        this.restaurantId = restaurant.getRestaurantId();
        this.restaurantName = restaurant.getRestaurantName();
        this.tags = restaurant.getTags().stream()
                .map(o -> o.getTag().getTagName())
                .collect(Collectors.toList());
        this.detailedPictureList = restaurant.getPictures().stream()
                .map(o -> o.getDetailPicture())
                .collect(Collectors.toList());
        this.address = address;
        this.thumbnailPictureUrl = thumbnailPictureUrl;
        this.flavorValue = restaurant.getFlavorRatio();
        this.moodValue = restaurant.getMoodRatio();
        this.serviceValue = restaurant.getServiceRatio();
        this.reasonable = restaurant.isReasonable();

        //default
        this.isLike = false;

        //default
        this.emotion = "neutral";

        //default
        this.fitMukbti = "default";
    }

}
