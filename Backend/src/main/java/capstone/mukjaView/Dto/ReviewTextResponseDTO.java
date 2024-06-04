package capstone.mukjaView.Dto;

import capstone.mukjaView.Domain.Restaurant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewTextResponseDTO {
    private String review;
    private String emotion;
    private double flavorValue;
    private double moodValue;
    private double serviceValue;
    private boolean reasonable;

    public ReviewTextResponseDTO(Restaurant restaurant) {
        this.flavorValue = restaurant.getFlavorRatio();
        this.moodValue = restaurant.getMoodRatio();
        this.serviceValue = restaurant.getServiceRatio();
        this.reasonable = restaurant.isReasonable();
    }
}
