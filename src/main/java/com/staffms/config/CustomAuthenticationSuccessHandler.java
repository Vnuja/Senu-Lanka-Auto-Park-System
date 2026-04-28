package com.staffms.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectUrl = "/dashboard";

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            if (role.equals("ROLE_SYSTEM_ADMIN")) {
                redirectUrl = "/admin/dashboard";
                break;
            } else if (role.equals("ROLE_HR_MANAGER")) {
                redirectUrl = "/hr/dashboard";
                break;
            } else if (role.equals("ROLE_DEPT_MANAGER")) {
                redirectUrl = "/manager/dashboard";
                break;
            } else if (role.equals("ROLE_STAFF")) {
                redirectUrl = "/staff/dashboard";
                break;
            } else if (role.equals("ROLE_IT_SUPPORT")) {
                redirectUrl = "/it/dashboard";
                break;
            } else if (role.equals("ROLE_ORG_MANAGEMENT")) {
                redirectUrl = "/analytics/dashboard";
                break;
            }
        }

        response.sendRedirect(request.getContextPath() + redirectUrl);
    }
}
