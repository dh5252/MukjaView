package capstone.mukjaView.Repository;

import capstone.mukjaView.Domain.Restaurant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Page<Restaurant> findAll(Pageable pageable);
    Page<Restaurant> findByLatitudeBetweenAndLongitudeBetween(double minLat, double maxLat, double minLong, double maxLong, Pageable pageable);

    Page<Restaurant> findByRestaurantNameContaining(String keyword, Pageable pageable);
}
