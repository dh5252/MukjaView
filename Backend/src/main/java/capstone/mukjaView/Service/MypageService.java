package capstone.mukjaView.Service;

import capstone.mukjaView.Domain.Comment;
import capstone.mukjaView.Domain.Restaurant;
import capstone.mukjaView.Domain.User;
import capstone.mukjaView.Domain.UserLikeRestaurant;
import capstone.mukjaView.Dto.CommentResponseDTO;
import capstone.mukjaView.Dto.MapPageRestaurantResponse;
import capstone.mukjaView.Repository.CommentRepository;
import capstone.mukjaView.Repository.RestaurantRepository;
import capstone.mukjaView.Repository.UserLikeRestaurantRepository;
import capstone.mukjaView.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MypageService {
    private final CommentRepository commentRepository;
    private final UserLikeRestaurantRepository userLikeRestaurantRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    @Transactional
    public int addComment(Long restaurantId, String oauthIdentifier, String text) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        if (restaurantOptional.isEmpty())
            return 1;
        Restaurant restaurant = restaurantOptional.get();
        User user = userRepository.findByUsername(oauthIdentifier);
        if (user == null)
            return 1;

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setRestaurant(restaurant);
        comment.setComment(text);
        comment.setLikeCount(0);

        commentRepository.save(comment);
        return 0;
    }

    @Transactional
    public int changeLikeStatus(Long restaurantId, String oauthIdentifier, boolean change) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        if (restaurantOptional.isEmpty())
            return 1;
        Restaurant restaurant = restaurantOptional.get();
        User user = userRepository.findByUsername(oauthIdentifier);
        if (user == null)
            return 1;

        UserLikeRestaurant userLikeRestaurant = userLikeRestaurantRepository.findByUserAndRestaurant(user, restaurant);

        if (change && userLikeRestaurant == null) {
            userLikeRestaurant = new UserLikeRestaurant();
            userLikeRestaurant.setRestaurant(restaurant);
            userLikeRestaurant.setUser(user);
            userLikeRestaurantRepository.save(userLikeRestaurant);
            return 0;
        }
        else if (!change && userLikeRestaurant != null) {
            userLikeRestaurantRepository.deleteById(userLikeRestaurant.getUserRestaurantId());
            return 0;
        }
        return 0;
    }

    @Transactional(readOnly = true)
    public List<MapPageRestaurantResponse> getLikeRestaurants(String oauthIdentifier) {
        User user = userRepository.findByUsername(oauthIdentifier);
        if (user == null)
            return null;
        return user.getLikeRestaurants().stream()
                .map(o -> o.getRestaurant())
                .collect(Collectors.toList())
                .stream()
                .map(o -> new MapPageRestaurantResponse(o, user))
                .collect(Collectors.toList());
    }

    @Transactional
    public int deleteComment(Long commentId) {

        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isEmpty())
            return 1;

        commentRepository.delete(commentOptional.get());
        return 0;
    }
}

