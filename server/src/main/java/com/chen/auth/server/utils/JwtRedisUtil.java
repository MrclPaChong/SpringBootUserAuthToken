package com.chen.auth.server.utils;/**
 * Created by Administrator on 2019/9/10.
 */

import com.chen.auth.api.enums.StatusCode;
import com.chen.auth.server.enums.Constant;
import io.jsonwebtoken.*;
import org.bouncycastle.util.encoders.Base64;
import org.joda.time.DateTime;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

/**
 * jwt+redis通用工具类
 * @Author:chen (SteadyJack)
 * @Date: 2019/9/10 21:51
 **/
public class JwtRedisUtil {

    //生成密钥
    public static SecretKey generalKey(){
        byte[] encodedKey=Base64.decode(Constant.JWT_SECRET);
        SecretKey key=new SecretKeySpec(encodedKey,0,encodedKey.length,"AES");
        return key;
    }

    //创建token
    public static String createJWT(final String id,final String subject){
        //定义生成签名的算法
        SignatureAlgorithm algorithm=SignatureAlgorithm.HS256;
        //定义生成签名的密钥
        SecretKey key=generalKey();

        Date now=DateTime.now().toDate();
        //借助于第三方依赖组件jwt的api来实现
        JwtBuilder builder=Jwts.builder()
                //用户id
                .setId(id)
                //主体
                .setSubject(subject)
                //签发者
                .setIssuer(Constant.TOKEN_ISSUER)
                //签发时间
                .setIssuedAt(now)
                //签发时指定 加密算法、密钥
                .signWith(algorithm,key);

        //生成access token
        return builder.compact();
    }

    //验证解析token
    public static Claims validateJWT(final String accessToken) throws Exception{
        Claims claims=null;
        try {
            claims=parseJWT(accessToken);
        }catch (SignatureException e){
            throw new RuntimeException(StatusCode.TokenValidateCheckFail.getMsg());
        }catch (Exception e){
            throw new RuntimeException(StatusCode.TokenValidateCheckFail.getMsg());
        }
        return claims;
    }


    //解析token
    public static Claims parseJWT(final String accessToken) throws Exception{
        SecretKey key=generalKey();
        return Jwts.parser().setSigningKey(key).parseClaimsJws(accessToken).getBody();
    }
}










































