package capstone.mukjaView.Repository;

import capstone.mukjaView.Domain.CharacterReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterReviewRepository extends JpaRepository<CharacterReview, Long> {
    CharacterReview findByRestaurantRestaurantIdAndCharacterName(Long restaurantId, String characterName);
}
