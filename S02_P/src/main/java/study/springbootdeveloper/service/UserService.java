package study.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import study.springbootdeveloper.domain.User;
import study.springbootdeveloper.dto.AddUserRequest;
import study.springbootdeveloper.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;

    public Long save(AddUserRequest request) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); // 순환 참조 방지

        return userRepository.save(
                User.builder()
                        .email(request.email())
                        .password(encoder.encode(request.password()))
                        .build()
        ).getId();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}
