//package study.springbootdeveloper.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import study.springbootdeveloper.config.jwt.TokenProvider;
//import study.springbootdeveloper.config.oauth.OAuth2UserCustomService;
//import study.springbootdeveloper.repository.RefreshTokenRepository;
//import study.springbootdeveloper.service.UserService;
//
//import static org.springframework.boot.security.autoconfigure.web.servlet.PathRequest.toH2Console;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class WebSecurityConfig {
//
//    private final OAuth2UserCustomService oAuth2UserCustomService;
//    private final TokenProvider tokenProvider;
//    private final RefreshTokenRepository refreshTokenRepository;
//    private final UserService userService;
//
//    @Bean
//    public WebSecurityCustomizer configure() {
//        return (web) -> web.ignoring()
//                .requestMatchers(toH2Console())
//                .requestMatchers("/static/**");
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http.authorizeHttpRequests((auth) -> auth
//                .requestMatchers(
//                        "/login",
//                        "/signup",
//                        "/user"
//                ).permitAll()
//                .anyRequest().authenticated()
//        );
//
//        http.formLogin((login) -> login
//                .loginPage("/login")
//                .defaultSuccessUrl("/articles")
//        );
//
//        http.logout((logout) -> logout
//                .logoutSuccessUrl("/login")
//                .invalidateHttpSession(true) // 로그아웃 이후 해당 사용자의 세션을 전체 삭제할지 여부 확인
//        );
//
//        http.csrf(AbstractHttpConfigurer::disable);
//
//
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
