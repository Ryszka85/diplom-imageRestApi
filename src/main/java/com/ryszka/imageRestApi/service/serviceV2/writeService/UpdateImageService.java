package com.ryszka.imageRestApi.service.serviceV2.writeService;

import com.ryszka.imageRestApi.dao.ImageDAO;
import com.ryszka.imageRestApi.dao.TagDAO;
import com.ryszka.imageRestApi.dao.UserDAO;
import com.ryszka.imageRestApi.errorHandling.EntityNotFoundException;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.persistenceEntities.ImageEntity;
import com.ryszka.imageRestApi.persistenceEntities.TagEntity;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.service.dto.ImageDTO;
import com.ryszka.imageRestApi.service.dto.TagDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UpdateImageService {
    private TagDAO tagDAO;
    private ImageDAO imageDAO;
    private UserDAO userDAO;
    private final Logger logger =
            LoggerFactory.getLogger(UpdateImageService.class);

    public UpdateImageService(TagDAO tagDAO, ImageDAO imageDAO, UserDAO userDAO) {
        this.tagDAO = tagDAO;
        this.imageDAO = imageDAO;
        this.userDAO = userDAO;
    }

    public void setTags(ImageDTO imageDTO) {
        System.out.println(imageDTO);
        if (imageDTO != null &&
                imageDTO.getTags() != null &&
                imageDTO.getUserId() != null) {
            UserEntity userEntity = getUserEntityOrThrow(imageDTO);
            ImageEntity imageEntity = getImageEntityOrThrow(imageDTO);
            imageEntity.setUserEntity(userEntity);
            List<String> tagIDs = mapToIdList(imageDTO);
            getTagEntitiesOrThrow(tagIDs).forEach(tagEntity ->
                    imageEntity.getTags().add(tagEntity));
            imageDAO.saveImage(imageEntity);
        } else throw new IllegalArgumentException(
                ErrorMessages.INVALID_ARGUMENTS.getMessage());
    }


    private List<String> mapToIdList(ImageDTO imageDTO) {
        return imageDTO.getTags()
                .stream()
                .map(TagDTO::getTagId)
                .collect(Collectors.toList());
    }

    private List<TagEntity> getTagEntitiesOrThrow(List<String> tagIDs) {
        return tagDAO.getByTagIdList(tagIDs)
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorMessages.NOT_FOUND_BY_EID.getMessage()));
    }

    private ImageEntity getImageEntityOrThrow(ImageDTO imageDTO) {
        return imageDAO.getImagesByUserId(imageDTO.getImageId())
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorMessages.NOT_FOUND_BY_EID.getMessage()));
    }

    private UserEntity getUserEntityOrThrow(ImageDTO imageDTO) {
        return userDAO.findUserEntityByUserId(imageDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(
                        ErrorMessages.NOT_FOUND_BY_EID.getMessage()));
    }
}
