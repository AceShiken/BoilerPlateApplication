package com.demo.security;

import com.demo.entity.Role;
import com.demo.entity.User;
import com.demo.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    public static final String ATTR_USER = "currentUser";

    private final AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (!(handler instanceof HandlerMethod method)) return true;

        Auth auth = AnnotatedElementUtils.findMergedAnnotation(method.getMethod(), Auth.class);
        if (auth == null) {
            // Also check class-level
            auth = AnnotatedElementUtils.findMergedAnnotation(method.getBeanType(), Auth.class);
        }
        if (auth == null) {
            return true; // endpoint does not require auth
        }

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Missing or invalid Authorization header\"}");
            return false;
        }
        String bearer = header.substring("Bearer ".length()).trim();

        try {
            User user = authService.validateTokenAndGetUser(bearer);
            request.setAttribute(ATTR_USER, user);

            // role check
            Role required = auth.role();
            if (required == Role.ADMIN && user.getRole() != Role.ADMIN) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("{\"error\":\"Admin role required\"}");
                return false;
            }
            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
            return false;
        }
    }
}

