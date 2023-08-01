package com.joolove.core.controller;

import com.joolove.core.domain.member.User;
import com.joolove.core.dto.query.IGoodsView;
import com.joolove.core.dto.request.SignInRequest;
import com.joolove.core.repository.PasswordRepository;
import com.joolove.core.repository.SocialLoginRepository;
import com.joolove.core.service.EmailServiceImpl;
import com.joolove.core.service.GoodsService;
import com.joolove.core.service.SMSServiceImpl;
import com.joolove.core.service.UserService;
import com.joolove.core.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*", allowCredentials = "true", maxAge = 3600)
public class APIController {
    private final UserService userService;
    private final EmailServiceImpl emailService;
    private final SMSServiceImpl smsService;
    private final GoodsService goodsService;
    private final SocialLoginRepository socialLoginRepository;
    private final PasswordUtils passwordUtils;

    @GetMapping("/api/v1/user/authentication")
    public ResponseEntity<Boolean> checkAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }

        return new ResponseEntity<>(false, HttpStatus.OK);
    }

    @PostMapping("/api/v1/authentication-code/email")
    public ResponseEntity<?> getAuthenticationCodeEmail(@Valid @RequestBody String email)
            throws Exception {
        String code = emailService.sendAuthCode(email);
        return ResponseEntity.ok().body(code);
    }

    @PostMapping("/api/v1/authentication-code/sms")
    public ResponseEntity<?> getAuthenticationCodeSMS(@Valid @RequestBody String phoneNumber)
            throws Exception {
        String code = smsService.sendAuthCode(phoneNumber);
        return ResponseEntity.ok().body(code);
    }

    @PostMapping("/api/v1/authentication-code/email/check")
    public ResponseEntity<?> checkAuthenticationCodeEmail(@Valid @RequestBody String code) {
        if (Objects.equals(emailService.getAuthCode(), code)) {
            return ResponseEntity.ok().body("valid");
        }
        return ResponseEntity.ok().body("invalid");
    }

    @PostMapping("/api/v1/authentication-code/sms/check")
    public ResponseEntity<?> checkAuthenticationCodeSMS(@Valid @RequestBody String code) {
        if (Objects.equals(smsService.getAuthCode(), code)) {
            return ResponseEntity.ok().body("valid");
        }
        return ResponseEntity.ok().body("invalid");
    }

    @PostMapping("/api/v1/email/check")
    public ResponseEntity<?> checkEmail(@Valid @RequestBody String email) {
        User user = userService.findByUsername(email);
        if (user == null) {
            return ResponseEntity.ok().body("invalid");
        }

        if (socialLoginRepository.existsByUser(user)) {
            return ResponseEntity.ok().body("valid-incorrect");
        }

        return ResponseEntity.ok().body("valid-correct");
    }

    @PostMapping("/api/v1/password/check")
    public ResponseEntity<?> checkPassword(@Valid @RequestBody SignInRequest request) {
        User user = userService.findByUsername(request.getUsername());
        String encodedPassword = user.getPassword().getPw();

        if (passwordUtils.matches(request.getPassword(), encodedPassword)) {
            return ResponseEntity.ok().body("valid");
        }

        return ResponseEntity.ok().body("invalid");
    }

    // 상품 검색 : 직접 검색하거나 카테고리를 선택할 때
    @GetMapping("/api/v1/goods")
    public ResponseEntity<List<IGoodsView>> getGoodsList(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sort", required = false) String sort) {
        return ResponseEntity.ok().body(goodsService.findGoodsList(name, type, page, size, sort));
    }

}
