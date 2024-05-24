package capstone.mukjaView.Dto;

import capstone.mukjaView.Domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDTO {
    private String oauthIdentifier;
    private String realName;
    private String email;
    private String nickname;
    private String neutralImageUrl;
    private String smileImageUrl;
    private String sadImageUrl;
    private String mukbti;
    private boolean init;

    public UserInfoDTO(User user) {
        this.oauthIdentifier = user.getUsername();
        this.realName = user.getName();
        this.email = user.getEmail();
        this.nickname = user.getNickName();
        this.neutralImageUrl = user.getNeutralPicture();
        this.smileImageUrl = user.getSmilePicture();
        this.sadImageUrl = user.getSadPicture();
        this.init = user.isInit();
        this.mukbti = user.getMukbti();
    }
}
