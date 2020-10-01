package com.ryszka.imageRestApi.util.mapper.mapStrategies;

import com.ryszka.imageRestApi.util.mapper.ObjectMapper;
import com.ryszka.imageRestApi.persistenceEntities.ImageEntity;
import com.ryszka.imageRestApi.viewModels.response.UserDetailsResponseModel;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.viewModels.response.UserImageResponseModel;
import com.ryszka.imageRestApi.service.dto.TagDTO;
import com.ryszka.imageRestApi.util.PathGenerator;

import java.util.List;

public class ImageEntityToImageRespModel implements MapStrategy<ImageEntity, UserImageResponseModel> {

    @Override
    public UserImageResponseModel map(ImageEntity source) {
        UserEntity userEntity = source.getUserEntity();
        UserDetailsResponseModel owner = ObjectMapper.mapByStrategy(userEntity, new UserEntityToUserDetailsResponseModel());
        List<TagDTO> tags = ObjectMapper.mapToList(source.getTags(), new TagEntitiesToTagDtoList());
        String fileAccessLink =
                PathGenerator.generateFileAccessLink(userEntity.getUserId() + "/" + source.getName());
        return new UserImageResponseModel(
                source.getName(),
                fileAccessLink,
                source.getImageId(),
                owner,
                tags
        );
    }
}
