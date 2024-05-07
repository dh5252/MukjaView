package capstone.mukjaView.Dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserBasicInfoDTO {
    private String nickName;
    private MultipartFile neutralPicture;
    private String mukbti;
}
