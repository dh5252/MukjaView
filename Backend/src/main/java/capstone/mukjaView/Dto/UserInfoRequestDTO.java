package capstone.mukjaView.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoRequestDTO {
    private String nickname;
    private String mukbti;
    private boolean init;
    private String smilePictureUrl;
    private String neutralPictureUrl;
    private String sadPictureUrl;
}
