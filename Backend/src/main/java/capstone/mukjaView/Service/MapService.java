package capstone.mukjaView.Service;

import capstone.mukjaView.Domain.Restaurant;
import capstone.mukjaView.Repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MapService {
    private final RestaurantRepository restaurantRepository;



}
