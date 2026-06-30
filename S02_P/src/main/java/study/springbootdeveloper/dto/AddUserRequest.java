package study.springbootdeveloper.dto;

public record AddUserRequest(
        String email,
        String password
) {
}
