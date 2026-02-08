package com.example.productservice.client;

import com.example.productservice.dto.external.UserServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserServiceClient {

    private final RestTemplate restTemplate;

    @Value("${app.user-service.url:http://localhost:8081}")
    private String userServiceUrl;

    /**
     * Gọi HTTP GET sang UserService để lấy thông tin user
     * @param userId ID của user cần lấy
     * @return UserServiceResponse hoặc null nếu có lỗi
     */
    public UserServiceResponse getUserById(Long userId) {
        String url = userServiceUrl + "/users/" + userId;
        
        try {
            log.info("🌐 Calling UserService via HTTP - URL: {}", url);
            UserServiceResponse response = restTemplate.getForObject(url, UserServiceResponse.class);
            log.info("✅ Successfully fetched user from UserService - UserId: {}, Name: {}", 
                    response != null ? response.getId() : null, 
                    response != null ? response.getName() : null);
            return response;
        } catch (RestClientException e) {
            log.error("❌ Failed to call UserService - URL: {}, Error: {}", url, e.getMessage());
            return null;
        }
    }

    /**
     * Kiểm tra xem user có tồn tại hay không
     * @param userId ID của user cần kiểm tra
     * @return true nếu user tồn tại, false nếu không
     */
    public boolean isUserExists(Long userId) {
        UserServiceResponse user = getUserById(userId);
        return user != null;
    }
}
