package capstone.mukjaView.Repository;

import capstone.mukjaView.Domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
