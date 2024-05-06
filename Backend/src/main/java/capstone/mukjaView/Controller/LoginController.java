package capstone.mukjaView.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
    @GetMapping("/api/v1/init")
    @ResponseBody
    public Boolean getInit() {
        return false;
    }

    @GetMapping("/api/v1/check")
    @ResponseBody
    public String checkAPI() {
        return "testAPI";
    }

    @GetMapping("/test")
    @ResponseBody
    public String testAPI() {
        return "test";
    }
}

