package capstone.mukjaView.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeRequestDTO {
    private String OauthIdentifier;
    private boolean like;
}
