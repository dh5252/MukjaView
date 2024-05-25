package capstone.mukjaView.Service;

import capstone.mukjaView.Domain.Restaurant;

import capstone.mukjaView.Domain.RestaurantTag;
import capstone.mukjaView.Repository.RestaurantRepository;
import capstone.mukjaView.Repository.RestaurantTagRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MapService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantTagRepository restaurantTagRepository;

    public Page<Restaurant> getRestaurantsBySizeAndPage(int page, double minLat, double maxLat, double minLong, double maxLong) {
        Pageable pageable = PageRequest.of(page, 10);
        return restaurantRepository.findByLatitudeBetweenAndLongitudeBetween(minLat, maxLat, minLong, maxLong, pageable);
    }

    public List<Restaurant> getRestaurantsByTagAndPage(int page, String tag) {
        Pageable pageable = PageRequest.of(page, 10);

        List<RestaurantTag> restaurantTags = restaurantTagRepository.findByTag(tag, pageable)
                .getContent();

        return restaurantTags.stream()
                .map(o -> o.getRestaurant())
                .collect(Collectors.toList());
    }

    public List<Restaurant> getRestaurantsByNameAndPage(int page, String name) {
        Pageable pageable = PageRequest.of(page, 10);

        return restaurantRepository.findByRestaurantNameContaining(name, pageable)
                .getContent();
    }

}
