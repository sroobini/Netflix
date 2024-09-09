package com.identity.platform.utils;

import com.identity.platform.error.SIGNING_KEY_LOAD_ERROR;
import com.identity.platform.model.common.TokenContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

import io.jsonwebtoken.*;

import static com.identity.platform.common.CONSTANTS.*;

@Component
public class JwtUtils {

    @Autowired
    FileUtils fileUtils;


    public String generateJwtToken(TokenContext tokenContext) {
        return createJwt(tokenContext, PRIVATE_KEY_FILE_PATH);
    }

    public String createJwt(TokenContext tokenContext, String pemFilePath) throws SIGNING_KEY_LOAD_ERROR {
        try {

            PrivateKey privateKey = loadPrivateKey(pemFilePath);

            return Jwts.builder()
                    .setIssuer(ISSUER)
                    .setSubject(tokenContext.getSubject().getUserName())
                    .setAudience(AUD_URL)
                    .claim("scope", tokenContext.getScope())
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 15 * 60)) // 15 min expiration
                    .signWith(privateKey, SignatureAlgorithm.ES256)
                    .compact();
        } catch(NoSuchAlgorithmException | InvalidKeySpecException  exception ) {
            throw new SIGNING_KEY_LOAD_ERROR( exception.getMessage());
        }

    }

    private  PrivateKey loadPrivateKey(String pemFilePath) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String privateKeyPEM = fileUtils.loadFileAsString(pemFilePath, StandardCharsets.UTF_8);
        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "");
        privateKeyPEM = privateKeyPEM.replace("-----END PRIVATE KEY-----", "");
        privateKeyPEM = privateKeyPEM.replaceAll("\\s+", "");

        byte[] decoded = Base64.getDecoder().decode(privateKeyPEM);

        KeyFactory kf = KeyFactory.getInstance("EC");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
        return kf.generatePrivate(keySpec);
    }
}


