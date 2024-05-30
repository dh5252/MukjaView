package capstone.mukjaView.Repository;

import capstone.mukjaView.Domain.RestaurantTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantTagRepository extends JpaRepository<RestaurantTag, Long> {
    Page<RestaurantTag> findByTagTagName(String tag, Pageable pageable);
}
