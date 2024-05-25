package capstone.mukjaView.Repository;

import capstone.mukjaView.Domain.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, String> {
    Page<Tag> findByTagName(String tag, Pageable pageable);
}
