package com.example.todolist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

/**
 * 安全配置類
 * 
 * 配置應用的安全相關設置，包括 CORS、CSRF、密碼加密器和請求授權規則等。
 * 使用 Spring Security 框架實現安全防護。
 */
@Configuration
@EnableWebSecurity  // 啟用 Spring Security 的 Web 安全功能
public class SecurityConfig {
    
    /**
     * 配置密碼加密器
     * 
     * 使用 BCrypt 演算法對密碼進行單向加密，提高安全性
     * BCrypt 會自動處理鹽值和多次哈希，適合密碼存儲
     * 
     * @return 配置好的 PasswordEncoder 實例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置 Security 過濾鏈
     * 
     * 定義安全過濾規則，包括 CSRF 保護、CORS 支持和請求授權規則
     * 
     * @param http HttpSecurity 配置對象
     * @return 配置好的 SecurityFilterChain
     * @throws Exception 配置過程中可能拋出的異常
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用 CSRF 保護，因為我們使用 JWT 或 Session 進行認證
            .csrf(csrf -> csrf.disable())
            
            // 啟用 CORS 支持，具體配置在 corsConfigurer 方法中
            .cors(Customizer.withDefaults())
            
            // 請求授權規則
            .authorizeHttpRequests(auth -> auth
                // 公開端點，無需認證
                .requestMatchers("/api/users/register", "/api/users/login", "/api/users/password-reset/**").permitAll()
                
                // 其他所有請求都允許訪問（實際授權在控制器中通過 session 檢查）
                .anyRequest().permitAll()
            );
            
        return http.build();
    }

    /**
     * 配置 CORS (跨源資源共享)
     * 
     * 允許前端應用從不同的域訪問 API
     * 
     * @return 配置好的 WebMvcConfigurer
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // 所有路徑
                    .allowedOrigins("http://localhost:5173")  // 允許的前端來源
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 允許的 HTTP 方法
                    .allowedHeaders("*")  // 允許的請求頭
                    .allowCredentials(true);  // 允許發送憑證（Cookies）
            }
        };
    }
}