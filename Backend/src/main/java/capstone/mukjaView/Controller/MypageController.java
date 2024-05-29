package capstone.mukjaView.Controller;

import capstone.mukjaView.Dto.CommentRequestDTO;
import capstone.mukjaView.Dto.CustomOAuth2User;
import capstone.mukjaView.Dto.LikeRequestDTO;
import capstone.mukjaView.Service.MypageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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


    // 좋아요 누른 거 조회
    // 자기 댓글 삭제
}
