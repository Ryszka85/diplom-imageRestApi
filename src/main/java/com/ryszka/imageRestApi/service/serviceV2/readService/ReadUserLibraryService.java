package com.ryszka.imageRestApi.service.serviceV2.readService;
import com.ryszka.imageRestApi.dao.ImageDAO;
import com.ryszka.imageRestApi.dao.TagDAO;
import com.ryszka.imageRestApi.dao.UserDAO;
import com.ryszka.imageRestApi.errorHandling.EntityNotFoundException;
import com.ryszka.imageRestApi.util.mapper.mapStrategies.ImageEntitiesToImageRespModels;
import com.ryszka.imageRestApi.util.mapper.mapStrategies.UserEntityToImageRespModels;
import com.ryszka.imageRestApi.util.mapper.ObjectMapper;
import com.ryszka.imageRestApi.persistenceEntities.ImageEntity;
import com.ryszka.imageRestApi.persistenceEntities.TagEntity;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.viewModels.response.UserImageResponseModel;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ReadUserLibraryService {
    private final Logger logger =
            LoggerFactory.getLogger(ReadUserLibraryService.class);
    private final UserDAO userDAO;
    private final TagDAO tagDAO;
    private final ImageDAO imageDAO;

    public ReadUserLibraryService(UserDAO userDAO, TagDAO tagDAO, ImageDAO imageDAO) {
        this.userDAO = userDAO;
        this.tagDAO = tagDAO;
        this.imageDAO = imageDAO;
    }

    /*public ReadUserLibraryService(UserDAO userDAO, TagDAO tagDAO) {
        this.userDAO = userDAO;
        this.tagDAO = tagDAO;
    }*/

    // TODO: 04.09.2020 Add Download link wo imageResponseModel 

    public Optional<List<UserImageResponseModel>> getUserImages(String userId) {
        Optional<UserEntity> userEntityOpt = userDAO.findUserEntityByUserId(userId);
        if (userEntityOpt.isEmpty())
            throw new EntityNotFoundException(
                    ErrorMessages.NOT_FOUND_BY_EID.getMessage() + userId);
        UserEntity userEntity = userEntityOpt.get();
        List<UserImageResponseModel> output = ObjectMapper.mapByStrategy(
                userEntity,
                new UserEntityToImageRespModels()
        );
        return Optional.ofNullable(output);
    }


    public Optional<List<UserImageResponseModel>> getImagesByTagNamePageable(String tag, int pageReq) {
        Optional<TagEntity> resultOpt = this.tagDAO.getTagByName(tag);
        if (resultOpt.isEmpty()) return Optional.empty();
        TagEntity tagEntity = resultOpt.get();
        // TODO: 15.09.2020 Implement pageable in this method -> call imageDAO.getImagesByTagName()
        //  and implement pageable in repository
        /*PageRequest pageRequest = PageRequest.of(5, 5);
        Optional<List<ImageEntity>> imagesByTagName = imageDAO.getImagesByTagName(tagEntity, pageRequest);
        imagesByTagName
                .ifPresent(imageEntities -> System.out.println(imageEntities.size()));*/
        List<ImageEntity> imageEntities = tagEntity.getImageEntities();
        List<UserImageResponseModel> userImageResponseModels = ObjectMapper.mapByStrategy(
                imageEntities,
                new ImageEntitiesToImageRespModels()
        );
        return Optional.ofNullable(userImageResponseModels);
    }

}
