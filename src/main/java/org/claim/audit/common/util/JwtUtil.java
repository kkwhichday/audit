package org.claim.audit.common.util;



import org.claim.audit.common.exception.BusinessException;
import org.claim.audit.common.vo.ResponseData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;


@Component
public class JwtUtil {
	@Value("${token.sercetKey}")
    public String sercetKey="gaga12345";
	@Value("${token.keeptime}")
    public long  keeptime;

	private static final String EXP = "exp";

    private static final String PAYLOAD = "payload";

    //加密，传入一个对象和有效期
    public  <T> String sign(T object) {
        try {
            final JWTSigner signer = new JWTSigner(sercetKey);
            final Map<String, Object> claims = new HashMap<String, Object>();
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(object);
            claims.put(PAYLOAD, jsonString);
            claims.put(EXP, System.currentTimeMillis() + keeptime*1000*60*60);
            return signer.sign(claims);
        } catch(Exception e) {
            return null;
        }
    }

    //解密，传入一个加密后的token字符串和解密后的类型
    public <T> T unsign(String jwt, Class<T> classT) throws Exception {
        final JWTVerifier verifier = new JWTVerifier(sercetKey);
      
        final Map<String,Object> claims= (Map<String, Object>) verifier.verify(jwt);
        if (claims.containsKey(EXP) && claims.containsKey(PAYLOAD)) {
            long exp = (Long)claims.get(EXP);
            long currentTimeMillis = System.currentTimeMillis();
            if (exp > currentTimeMillis) {
                String json = (String)claims.get(PAYLOAD);
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(json, classT);
            }else{
            	throw new BusinessException(ResponseData.TokenTimeout());
            }
        }else{
        	throw new BusinessException(ResponseData.TokenError());
        }
    }


}