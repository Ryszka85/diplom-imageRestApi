package com.ryszka.imageRestApi.service.serviceV2.readService;

import com.ryszka.imageRestApi.dao.UserDAO;
import com.ryszka.imageRestApi.errorHandling.EntityNotFoundException;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.util.mapper.mapStrategies.ImageEntitiesToImageRespModels;
import com.ryszka.imageRestApi.util.mapper.mapStrategies.UserEntityToImageRespModels;
import com.ryszka.imageRestApi.viewModels.response.UserDetailsResponseModel;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.util.mapper.ObjectMapper;
import com.ryszka.imageRestApi.util.mapper.mapStrategies.UserEntityToUserDetailsResponseModel;
import com.ryszka.imageRestApi.viewModels.response.UserImageResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class ReadUserDetailsService {
    private final Logger logger =
            LoggerFactory.getLogger(ReadUserDetailsService.class);
    private final UserDAO userDAO;

    public ReadUserDetailsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserDetailsResponseModel getUserDetailsByUID(String userId) {
        logger.info("Attempting [ getUserDetailsByUID.. ]");
        Optional<UserEntity> optUserEntity = userDAO.findUserEntityByUserId(userId);
        if (optUserEntity.isEmpty())
            throw new EntityNotFoundException(
                    ErrorMessages.NOT_FOUND_BY_EID.getMessage());
        UserEntity userEntity = optUserEntity.get();
        UserDetailsResponseModel responseModel =
                ObjectMapper.mapByStrategy(userEntity, new UserEntityToUserDetailsResponseModel());
        List<UserImageResponseModel> userImages =
                ObjectMapper.mapByStrategy(userEntity, new UserEntityToImageRespModels());
        List<UserImageResponseModel> likes =
                ObjectMapper.mapByStrategy(userEntity.getLikes(), new ImageEntitiesToImageRespModels());
        responseModel.setImages(userImages);
        responseModel.setLikes(likes);
        return responseModel;
    }

    public UserDetailsResponseModel findUserByEmail(String email) {
        logger.info("Attempting [ findUserByEmail.. ]");
        Optional<UserEntity> byEmailOpt = userDAO.findByEmail(email);
        if (byEmailOpt.isEmpty()) throw new EntityNotFoundException(
                ErrorMessages.NOT_FOUND_BY_USERNAME.getMessage());
        UserEntity userEntity = byEmailOpt.get();
        return ObjectMapper.mapByStrategy(userEntity,
                new UserEntityToUserDetailsResponseModel());
    }

}
