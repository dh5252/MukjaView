package capstone.mukjaView.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {


    @PostMapping("/api/v1/users/{id}/info")
    public void updateUserInfo(@PathVariable Long id) {
        // tmp
    }

    @GetMapping("/api/v1/login")
    @ResponseBody
    public String checkAPI() {
        return "testAPI";
    }
}

