package capstone.mukjaView.Controller;

import capstone.mukjaView.Dto.CustomOAuth2User;
import capstone.mukjaView.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base.url}")
public class LoginController {

    private final UserService userService;


    @GetMapping("/v1/login/google")
    @Operation(summary = "google login link")
    public String googleHyperlinkAPI() {
        return "/oauth2/authorization/google";
    }
    @GetMapping("/v1/user/name")
    public ResponseEntity<String> returnUserName() {
        System.out.println("controller ok");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) auth.getPrincipal();
            String username = oAuth2User.getUsername();
            return new ResponseEntity<>(username, HttpStatus.OK);
        }
        return new ResponseEntity<>("Not login", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/v1/users/{name}/nickname")
    @Operation(summary = "update user nickname")
    public ResponseEntity<String> updateUserNickName(@PathVariable String name, @RequestBody HashMap<String, Object> body) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) auth.getPrincipal();
            String username = oAuth2User.getUsername();
            if (name != username)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username is invalid: hack!");
            String nickname = (String)body.get("nickname");
            if (userService.updateNickname(name, nickname) == 0)
                return ResponseEntity.noContent().build(); // 성공
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("jwt is invalid");
    }

//    @PostMapping("/api/v1/users/{name}/mukbti")
//    @Operation(summary = "update user mukbti")
//    public ResponseEntity<String> updateUserMukbti(@PathVariable String name, @RequestBody HashMap<String, Object> body) {
//
//    }

}

