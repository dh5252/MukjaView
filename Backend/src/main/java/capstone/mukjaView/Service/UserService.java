package capstone.mukjaView.Service;

import capstone.mukjaView.Domain.User;
import capstone.mukjaView.Dto.UserInfoDTO;
import capstone.mukjaView.Dto.UserInfoRequestDTO;
import capstone.mukjaView.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public int updateNickname(String name, String nickname) {
        User user = userRepository.findByUsername(name);
        if (user != null) {
            user.setNickName(nickname);
            userRepository.save(user);
            return 0;
        }
        return 1;
    }

    @Transactional
    public int updateMukbti(String name, String mukbti) {
        User user = userRepository.findByUsername(name);
        if (user != null) {
            user.setMukbti(mukbti);
            userRepository.save(user);
            return 0;
        }
        return 1;
    }

    @Transactional(readOnly = true)
    public User getUser(String userName) {
        User user = userRepository.findByUsername(userName);
        if (user != null)
            return user;
        return null;
    }

    @Transactional(readOnly = true)
    public UserInfoDTO returnUserInfo(String userName) {
        User user = userRepository.findByUsername(userName);
        if (user != null)
            return new UserInfoDTO(user);
        return null;
    }

    @Transactional
    public int patchUserInitToFalse(String name) {
        User user = userRepository.findByUsername(name);
        if (user != null) {
            user.setInit(false);
            userRepository.save(user);
            return 0;
        }
        return 1;
    }
    @Transactional
    public int patchUserInfo(String name, UserInfoRequestDTO userInfoRequestDTO) {
        User user = userRepository.findByUsername(name);
        if (user != null) {
            user.setMukbti(userInfoRequestDTO.getMukbti());
            user.setNickName(userInfoRequestDTO.getNickname());
            user.setNeutralPicture(userInfoRequestDTO.getNeutralPictureUrl());
            user.setSmilePicture(userInfoRequestDTO.getSmilePictureUrl());
            user.setSadPicture(userInfoRequestDTO.getSadPictureUrl());
            user.setInit(userInfoRequestDTO.isInit());
            userRepository.save(user);
            return 0;
        }
        return 1;
    }

}
