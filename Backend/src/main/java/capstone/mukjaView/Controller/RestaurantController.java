package capstone.mukjaView.Controller;

import capstone.mukjaView.Dto.CustomOAuth2User;
import capstone.mukjaView.Dto.ReviewPageResponse;
import capstone.mukjaView.Service.RestaurantService;
import capstone.mukjaView.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestaurantController {
    private final UserService userService;
    private final RestaurantService restaurantService;

    @GetMapping("/api/v1/restaurant/{restaurantId}/info")
    @Operation(summary = "get one restaurant info")
    public ResponseEntity<ReviewPageResponse> returnRestaurantInfo(
            @PathVariable Long restaurantId,
            @RequestParam String oauthIdentifier
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username;
        if (auth != null && auth.isAuthenticated()) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) auth.getPrincipal();
            username = oAuth2User.getUsername();
            if (!username.equals(oauthIdentifier))
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        else
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        ReviewPageResponse rtn = restaurantService.returnReviewPageResponse(restaurantId, username);
        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }
}
