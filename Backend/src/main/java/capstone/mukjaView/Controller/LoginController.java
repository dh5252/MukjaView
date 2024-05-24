package capstone.mukjaView.Controller;

import capstone.mukjaView.Dto.CustomOAuth2User;
import capstone.mukjaView.Dto.UserInfoDTO;
import capstone.mukjaView.Dto.UserInfoRequestDTO;
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
public class LoginController {

    private final UserService userService;
    @GetMapping("api/v1/user/oauth/identifier")
    @Operation(summary = "get user Oauth identifier")
    public ResponseEntity<String> returnUserOauthIdentifier() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) auth.getPrincipal();
            String username = oAuth2User.getUsername();
            return new ResponseEntity<>(username, HttpStatus.OK);
        }
        return new ResponseEntity<>("Not login", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/api/v1/user/info")
    @Operation(summary = "get user info")
    public ResponseEntity<UserInfoDTO> returnUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) auth.getPrincipal();
            UserInfoDTO rtn = userService.returnUserInfo(oAuth2User.getUsername());
            if (rtn != null)
                return new ResponseEntity<>(rtn, HttpStatus.OK);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("api/v1/users/{name}/nickname")
    @Operation(summary = "update user nickname")
    public ResponseEntity<String> updateUserNickName(@PathVariable String name, @RequestBody HashMap<String, Object> body) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) auth.getPrincipal();
            String username = oAuth2User.getUsername();
            if (!name.equals(username))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username is invalid: hack!");
            String nickname = (String)body.get("nickname");
            if (userService.updateNickname(name, nickname) == 0)
                return ResponseEntity.noContent().build(); // 성공
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("jwt is invalid");
    }

    @PostMapping("/api/v1/users/{name}/mukbti")
    @Operation(summary = "update user mukbti")
    public ResponseEntity<String> updateUserMukbti(@PathVariable String name, @RequestBody HashMap<String, Object> body) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) auth.getPrincipal();
            String username = oAuth2User.getUsername();
            if (!name.equals(username))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username is invalid: hack!");
            String mukbti = (String)body.get("mukbti");
            if (userService.updateMukbti(name, mukbti) == 0)
                return ResponseEntity.noContent().build(); // 성공
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("jwt is invalid");
    }

    @PatchMapping("/api/v1/users/{name}/init")
    @Operation(summary = "update user status init (true >> false)")
    public ResponseEntity<String> updateUserInit(@PathVariable String name) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) auth.getPrincipal();
            String username = oAuth2User.getUsername();
            if (!name.equals(username))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username is invalid: hack!");
            if (userService.patchUserInitToFalse(username) == 0)
                return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("jwt is invalid");
    }

    @PatchMapping("/api/v1/users/{name}/info")
    @Operation(summary = "update user info")
    public ResponseEntity<String> updateUserInfo(@PathVariable String name, @RequestBody UserInfoRequestDTO userInfoRequestDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) auth.getPrincipal();
            String username = oAuth2User.getUsername();
            if (!name.equals(username))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username is invalid: hack!");
            if (userService.patchUserInfo(username, userInfoRequestDTO) == 0)
                return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("jwt is invalid");
    }

}


