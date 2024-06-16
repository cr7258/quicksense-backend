package pro.quicksense.modules.util;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import pro.quicksense.modules.common.CommonConstant;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Get the token from the request header
        String token = request.getHeader(CommonConstant.X_ACCESS_TOKEN);

        if (StringUtils.isBlank(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is missing or has expired");
            return false;
        }

        try {
            // Verify the token
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();

            // Store the user information in the request attributes for subsequent use
            request.setAttribute("userId", claims.get("userId"));

            return true;
        } catch (JwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return false;
        }
    }
}
