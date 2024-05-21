package capstone.mukjaView.Repository;

import capstone.mukjaView.Domain.UserLikeRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLikeRestaurantRepository extends JpaRepository<UserLikeRestaurant, Long> {
}
