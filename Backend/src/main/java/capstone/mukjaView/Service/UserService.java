package capstone.mukjaView.Service;

import capstone.mukjaView.Domain.User;
import capstone.mukjaView.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public int updateNickname(String name, String nickname) {
        User user = userRepository.findByUsername(name);
        if (user != null) {
            user.setNickName(nickname);
            userRepository.save(user);
            return 0;
        }
        return 1;
    }

}
