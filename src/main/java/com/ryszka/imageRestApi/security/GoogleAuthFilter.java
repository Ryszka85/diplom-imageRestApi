package com.ryszka.imageRestApi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.ryszka.imageRestApi.config.AppContext;
import com.ryszka.imageRestApi.dao.UserDAO;
import com.ryszka.imageRestApi.errorHandling.EntityNotFoundException;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.service.serviceV2.readService.ReadUserDetailsService;
import com.ryszka.imageRestApi.standardMesages.StandardMessages;
import com.ryszka.imageRestApi.util.mapper.mapStrategies.LoginModelToUserEntity;
import com.ryszka.imageRestApi.viewModels.request.UserLoginRequest;
import com.ryszka.imageRestApi.viewModels.response.UserDetailsResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class GoogleAuthFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final Logger logger = LoggerFactory.getLogger(GoogleAuthFilter.class);
    private final UserDAO userDAO;
    private final ObjectMapper mapper;

    public GoogleAuthFilter(AuthenticationManager authenticationManager, UserDAO userDAO, ObjectMapper mapper) {
        this.authenticationManager = authenticationManager;
        this.userDAO = userDAO;
        this.mapper = mapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {

            UserLoginRequest userLoginRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), UserLoginRequest.class);
            NetHttpTransport netHttpTransport = new NetHttpTransport();
            JacksonFactory instance = JacksonFactory.getDefaultInstance();
            GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, instance)
                    .setAudience(Collections.singletonList(AppConfigProperties.GOOGLE_ID));
            GoogleIdToken idToken = GoogleIdToken.parse(verifier.getJsonFactory(), userLoginRequest.getTokenId());
            Optional<UserEntity> byEmailOpt = userDAO.findByEmail(userLoginRequest.getEmail());
            SecurityContext context = SecurityContextHolder.getContext();
            if (byEmailOpt.isEmpty()) {
                UserEntity userEntity = com.ryszka.imageRestApi.util.mapper.ObjectMapper.mapByStrategy(userLoginRequest, new LoginModelToUserEntity());
                userDAO.saveUserEntity(userEntity);
                context.setAuthentication(
                        new UsernamePasswordAuthenticationToken(userLoginRequest.getEmail(),null));
            } else {
                UserEntity userEntity = byEmailOpt.get();
                context.setAuthentication(new UsernamePasswordAuthenticationToken(
                        userEntity.getEmail(),
                        userEntity.getPassword(),
                        new ArrayList<>()
                ));
            }
            logger.info(StandardMessages.LOGIN_SUCCESS.getMsg());
            return context.getAuthentication();
        } catch (IOException | IllegalArgumentException e) {
            logger.error(ErrorMessages.LOGIN_FAIL.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }
}
