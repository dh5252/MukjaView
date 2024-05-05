package capstone.mukjaView.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
    @GetMapping("/api/v1/login")
    @ResponseBody
    public String myAPI() {

        return "my route";
    }

    @GetMapping("/test")
    @ResponseBody
    public String testAPI() {
        return "test";
    }
}

