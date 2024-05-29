package capstone.mukjaView.Repository;

import capstone.mukjaView.Domain.Restaurant;
import capstone.mukjaView.Domain.User;
import capstone.mukjaView.Domain.UserLikeRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLikeRestaurantRepository extends JpaRepository<UserLikeRestaurant, Long> {

    UserLikeRestaurant findByUserAndRestaurant(User user, Restaurant restaurant);

}
