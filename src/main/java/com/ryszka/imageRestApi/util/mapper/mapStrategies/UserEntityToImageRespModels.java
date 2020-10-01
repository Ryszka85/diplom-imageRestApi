package com.ryszka.imageRestApi.util.mapper.mapStrategies;

import com.ryszka.imageRestApi.util.mapper.ObjectMapper;
import com.ryszka.imageRestApi.viewModels.response.UserImageResponseModel;
import com.ryszka.imageRestApi.viewModels.response.UserDetailsResponseModel;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import com.ryszka.imageRestApi.service.dto.TagDTO;
import com.ryszka.imageRestApi.util.PathGenerator;

import java.util.List;
import java.util.stream.Collectors;

public class UserEntityToImageRespModels implements MapStrategy<UserEntity, List<UserImageResponseModel>> {

    @Override
    public List<UserImageResponseModel> map(UserEntity source) {
        return source
                .getImageEntities()
                .stream()
                .map(imageEntity -> {
                    String fileAccessLink = PathGenerator.generateFileAccessLink(
                            imageEntity.getPath() + "/" + imageEntity.getName());

                    UserDetailsResponseModel owner = ObjectMapper.mapByStrategy(source,
                            new UserEntityToUserDetailsResponseModel());

                    List<TagDTO> tags = ObjectMapper.mapToList(imageEntity.getTags(),
                            new TagEntitiesToTagDtoList());

                    return new UserImageResponseModel(imageEntity.getName(),
                            fileAccessLink,
                            imageEntity.getImageId(),
                            owner,
                            tags);
                })
                .collect(Collectors.toList());
    }
}
