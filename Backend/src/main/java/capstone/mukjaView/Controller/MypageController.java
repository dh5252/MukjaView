package capstone.mukjaView.Controller;

import capstone.mukjaView.Domain.Restaurant;
import capstone.mukjaView.Dto.*;
import capstone.mukjaView.Service.MypageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Delete;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;

    @PostMapping("/api/v1/{restaurantId}/comment")
    @Operation(summary = "add comment")
    public ResponseEntity<String> addComment(@PathVariable Long restaurantId, @RequestBody CommentRequestDTO commentRequestDTO) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) auth.getPrincipal();
            String username = oAuth2User.getUsername();
            if (!commentRequestDTO.getOauthIdentifier().equals(username))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("oauthIdentifier is invalid: hack!");

            if (mypageService.addComment(restaurantId, commentRequestDTO.getOauthIdentifier(), commentRequestDTO.getComment()) == 1)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad restaurant_id or user_id");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("jwt is invalid");
    }

    @PostMapping("/api/v1/{restaurantId}/like")
    @Operation(summary = "make like status or remove like status")
    public ResponseEntity<String> changeLikeStatus(@PathVariable Long restaurantId, @RequestBody LikeRequestDTO likeRequestDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) auth.getPrincipal();
            String username = oAuth2User.getUsername();
            if (!likeRequestDTO.getOauthIdentifier().equals(username))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("oauthIdentifier is invalid: hack!");

            if (mypageService.changeLikeStatus(restaurantId, likeRequestDTO.getOauthIdentifier(), likeRequestDTO.isLike()) == 1)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad restaurant_id or user_id");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("jwt is invalid");
    }

    @GetMapping("/api/v1/{oauthIdentifier}/like_list")
    @Operation(summary = "like list search")
    public ResponseEntity<List<MapPageRestaurantResponse>> returnLikeList(@PathVariable String oauthIdentifier) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) auth.getPrincipal();
            String username = oAuth2User.getUsername();
            if (!oauthIdentifier.equals(username))
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            List<MapPageRestaurantResponse> rtn = mypageService.getLikeRestaurants(oauthIdentifier);
            if (rtn == null)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(rtn, HttpStatus.OK);
        } else
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/api/v1/{restaurantId}/comment")
    @Operation(summary = "delete one restaurant's comment")
    public ResponseEntity<String> deleteComment(@PathVariable Long restaurantId, @RequestBody DeleteCommentRequestDTO deleteCommentRequestDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) auth.getPrincipal();
            String username = oAuth2User.getUsername();
            if (!deleteCommentRequestDTO.getOauthIdentifier().equals(username))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("oauthIdentifier is invalid: hack!");
            if (mypageService.deleteComment(deleteCommentRequestDTO.getCommentId()) == 1)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("oauthIdentifier is invalid: hack!");
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("jwt is invalid");
    }
}
