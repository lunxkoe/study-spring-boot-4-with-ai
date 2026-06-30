package study.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import study.springbootdeveloper.domain.User;
import study.springbootdeveloper.dto.AddUserRequest;
import study.springbootdeveloper.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long save(AddUserRequest request) {
        return userRepository.save(
                User.builder()
                        .email(request.email())
                        .password(passwordEncoder.encode(request.password()))
                        .build()
        ).getId();
    }
}
