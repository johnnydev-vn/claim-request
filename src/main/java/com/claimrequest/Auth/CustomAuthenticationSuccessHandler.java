package com.claimrequest.Auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
@EnableWebSecurity
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        /** Lấy danh sách các quyền của người dùng*/
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String redirectUrl = null;

        /** Duyệt qua các quyền để xác định trang điều hướng*/
        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

        /** Điều hướng dựa trên rank*/
            if (role.equals("ROLE_RANK_1")) {
                redirectUrl = "/admin/staff";  // Admin trang mặc định
                break;
            } else if (role.equals("ROLE_RANK_2")) {
                redirectUrl = "/approve/ForMyVetting";  // Approver trang mặc định
                break;
            } else if (role.equals("ROLE_RANK_3")) {
                redirectUrl = "/claim/view/draft";  // Claimer trang mặc định
                break;
            } else if (role.equals("ROLE_RANK_4")) {
                redirectUrl = "/finance/FinanceApproved";  // Finance trang mặc định
                break;
            }
        }

        /** Nếu không tìm thấy role phù hợp, điều hướng về trang mặc định*/
        if (redirectUrl == null) {
            redirectUrl = "/login";
        }

        /** Chuyển hướng đến trang phù hợp*/
        response.sendRedirect(redirectUrl);
    }
}
