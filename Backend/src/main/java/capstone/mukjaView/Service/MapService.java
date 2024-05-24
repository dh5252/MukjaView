package capstone.mukjaView.Service;

import capstone.mukjaView.Domain.Restaurant;
import capstone.mukjaView.Repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MapService {
    private final RestaurantRepository restaurantRepository;

    public Page<Restaurant> getRestaurantsBySize(int page, double minLat, double maxLat, double minLong, double maxLong) {
        Pageable pageable = PageRequest.of(page, 10);
        return restaurantRepository.findByLatitudeBetweenAndLongitudeBetween(minLat, maxLat, minLong, maxLong, pageable);
    }

}
