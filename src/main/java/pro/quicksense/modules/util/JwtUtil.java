package pro.quicksense.modules.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import pro.quicksense.modules.common.CommonConstant;
import pro.quicksense.modules.common.JeecgBootException;
import pro.quicksense.modules.common.Result;
import pro.quicksense.modules.common.SymbolConstant;



import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * @Author Scott
 * @Date 2018-07-12 14:23
 * @Desc JWT工具类
 **/
@Slf4j
public class JwtUtil {

    /**
     * Token有效期为7天（Token在reids中缓存时间为两倍）
     */
    public static final long EXPIRE_TIME = (7 * 12) * 60 * 60 * 1000;
    static final String WELL_NUMBER = SymbolConstant.WELL_NUMBER + SymbolConstant.LEFT_CURLY_BRACKET;

    /**
     * @param response
     * @param code
     * @param errorMsg
     */
    public static void responseError(ServletResponse response, Integer code, String errorMsg) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        // issues/I4YH95浏览器显示乱码问题
        httpServletResponse.setHeader("Content-type", "text/html;charset=UTF-8");
        Result jsonResult = new Result(code, errorMsg);
        jsonResult.setSuccess(false);
        OutputStream os = null;
        try {
            os = httpServletResponse.getOutputStream();
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setStatus(code);
            os.write(new ObjectMapper().writeValueAsString(jsonResult).getBytes("UTF-8"));
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            // 根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("username", username).build();
            // 效验TOKEN
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名,5min后过期
     *
     * @param username 用户名
     * @param secret   用户的密码
     * @return 加密的token
     */
    public static String sign(String username, String secret) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create().withClaim("username", username).withExpiresAt(date).sign(algorithm);

    }

    /**
     * 根据request中的token获取用户账号
     *
     * @param request
     * @return
     * @throws JeecgBootException
     */
    public static String getUserNameByToken(HttpServletRequest request) throws JeecgBootException {
        String accessToken = request.getHeader("X-Access-Token");
        String username = getUsername(accessToken);
        if (oConvertUtils.isEmpty(username)) {
            throw new JeecgBootException("未获取到用户");
        }
        return username;
    }

    /**
     * 从session中获取变量
     *
     * @param key
     * @return
     */
    public static String getSessionData(String key) {
        //${myVar}%
        //得到${} 后面的值
        String moshi = "";
        String wellNumber = WELL_NUMBER;

        if (key.indexOf(SymbolConstant.RIGHT_CURLY_BRACKET) != -1) {
            moshi = key.substring(key.indexOf("}") + 1);
        }
        String returnValue = null;
        if (key.contains(wellNumber)) {
            key = key.substring(2, key.indexOf("}"));
        }
        if (oConvertUtils.isNotEmpty(key)) {
            HttpSession session = SpringContextUtils.getHttpServletRequest().getSession();
            returnValue = (String) session.getAttribute(key);
        }
        //结果加上${} 后面的值
        if (returnValue != null) {
            returnValue = returnValue + moshi;
        }
        return returnValue;
    }

}
