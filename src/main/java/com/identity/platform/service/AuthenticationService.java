package com.identity.platform.service;

import com.identity.platform.dao.ClientCredentialsDAO;
import com.identity.platform.dao.UserDAO;
import com.identity.platform.error.CLIENT_CREDENTIALS_LOAD_ERROR;
import com.identity.platform.error.CODE_GENERATION_ERROR;
import com.identity.platform.exception.INVALID_CLIENT_EXCEPTION;
import com.identity.platform.exception.INVALID_CODE_CHALLENGE_EXCEPTION;
import com.identity.platform.exception.INVALID_USER_EXCEPTION;
import com.identity.platform.model.api.LoginRequest;
import com.identity.platform.model.common.Actor;
import com.identity.platform.model.common.Subject;
import com.identity.platform.model.common.TokenContext;
import com.identity.platform.model.common.User;
import com.identity.platform.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import static com.identity.platform.common.CONSTANTS.*;

/**
 * This class has the business logic to perform authentication of the User
 */
@Component
public class AuthenticationService {

    @Autowired
    UserDAO userDAO;

    @Autowired
    ClientCredentialsDAO clientCredentialsDAO;

    @Autowired
    ObjectMapperUtils objectMapperUtils;

    /**
     * This methods authenticates the Actor in the request headers
     * @param headers
     * @return
     * @throws CLIENT_CREDENTIALS_LOAD_ERROR
     */
    public Actor authenticateActor(String headers) throws CLIENT_CREDENTIALS_LOAD_ERROR{
        String[] clientCredentials = null;
        Actor actor = new Actor();
        actor.setClient_type("PUBLIC");
        if (headers != null) {
            clientCredentials = headers.split(":");
            validateClientCredentials(clientCredentials);
            actor.setClientId(clientCredentials[0]);
            actor.setClient_type("CONFIDENTIAL");
        }
        // allow empty auth header for client test purpose
        return actor;
    }

    /**
     * This method validates the client credentials
     * @param clientCredentials
     * @throws Exception
     */
    private void validateClientCredentials(String[] clientCredentials) throws INVALID_CLIENT_EXCEPTION ,
            CLIENT_CREDENTIALS_LOAD_ERROR {
        String clientId = clientCredentials[0];
        String clientSecret = clientCredentials[1];
        Map<String, String> allowedClientCredentials = clientCredentialsDAO.loadClientCredentials();
        if (clientId != null && clientSecret != null) {
            if (allowedClientCredentials.get(clientId) == null ||
                    !allowedClientCredentials.get(clientId).equals(clientSecret)) {
                throw new INVALID_CLIENT_EXCEPTION("Invalid Client");
            }
        }
    }

    /**
     *
     * This method validates the user from the LoginRequest
     *
     * @param request
     * @return
     * @throws Exception
     */
    public Subject validateUser(LoginRequest request) throws INVALID_USER_EXCEPTION {

        /**
         *  NOTE: I have hard coded the test username and password and verifying in the DB
         *  Ideally, these details will be coming from the LoginRequest.
         */
        String userName = TEST_USER_NAME;
        String passwordHash = TEST_USER_PASSWORD;

        //password is hashed by client and sent to the login api.
        // the server has only the hashed version of the password stored
        // only the hashed versions of the password is compared.

        Subject subject;
        User user = loadUserByUsername(userName);
        if (user == null) {
            throw new INVALID_USER_EXCEPTION("Invalid UserName");
        }
        validatePassword(user, passwordHash);
        subject = objectMapperUtils.mapToAuthPrincipal((user));
        return subject;
    }

    /**
     *
     * @param user
     * @param passwordHash
     * @throws INVALID_USER_EXCEPTION
     */
    private void validatePassword(User user, String passwordHash) throws INVALID_USER_EXCEPTION {

        if (!passwordHash.equals(user.getPasswordHash())) {
            throw new INVALID_USER_EXCEPTION("Invalid password");
        }
    }

    /**
     *
     * @param userName
     * @return
     */
    private User loadUserByUsername(String userName) {
        return userDAO.fetchUser(userName);
    }

    /**
     *
     * @param request
     * @return
     * @throws Exception
     */
    public String getAuthMechanism(LoginRequest request) throws INVALID_USER_EXCEPTION {
        String authMechanism = null;
        if (request != null && request.getCodeChallenge() != null &&
                request.getCodeChallengeMethod() != null) {
            authMechanism = PKCE_AUTH_MECHANISM;
        }
        if(authMechanism == null) {
            throw new INVALID_CODE_CHALLENGE_EXCEPTION("Missing Input Parameter");
        }
        return authMechanism;
    }

    /**
     *
     * @param request
     * @param tokenContext
     * @throws INVALID_CODE_CHALLENGE_EXCEPTION
     */
    public void validatePKCEParameters(LoginRequest request, TokenContext tokenContext)
            throws INVALID_CODE_CHALLENGE_EXCEPTION {
        if(!request.getCodeChallengeMethod().equals(SHA_ALGORITHM_NAME)) {
            throw new INVALID_CODE_CHALLENGE_EXCEPTION("Unsupported Code challenge method");
        }
        tokenContext.setCodeChallenge(request.getCodeChallenge());
        tokenContext.setCodeChallengeMethod(request.getCodeChallengeMethod());
    }

    /**
     * This method generates the Auth code using the TokenContext
     * @param tokenContext
     * @return
     * @throws CODE_GENERATION_ERROR
     */
    public String generateCode(TokenContext tokenContext) throws CODE_GENERATION_ERROR {
        // Extract relevant information from the token context
        String clientId = tokenContext.getActor().getClientId();
        String userId = tokenContext.getSubject().getId();

        // Generate a random code using UUID
        UUID uuid = UUID.randomUUID();
        String randomCode = uuid.toString().replace("-", "");
        tokenContext.setGsid(randomCode);

        // Construct the authorization code based on the random code and token context information
        String codeBuilder = clientId +
                "-" +
                userId +
                "-" +
                randomCode;

        // Generate a secure random salt
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        String authCode = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
            digest.update(codeBuilder.getBytes());
            byte[] hashedCode = digest.digest();
            // Base64-encode the hashed authorization code
            authCode = Base64.getUrlEncoder().withoutPadding().encodeToString(hashedCode);
            tokenContext.setNonce(authCode);
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new CODE_GENERATION_ERROR(noSuchAlgorithmException.getMessage());
        }
        return AUTH_CODE_PREFIX+authCode;
    }


}

