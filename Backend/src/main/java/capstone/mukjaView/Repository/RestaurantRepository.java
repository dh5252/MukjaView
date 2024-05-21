package capstone.mukjaView.Repository;

import capstone.mukjaView.Domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
