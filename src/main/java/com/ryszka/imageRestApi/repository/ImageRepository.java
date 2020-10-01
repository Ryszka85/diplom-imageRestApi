package com.ryszka.imageRestApi.repository;

import com.ryszka.imageRestApi.persistenceEntities.ImageEntity;
import com.ryszka.imageRestApi.persistenceEntities.TagEntity;
import com.ryszka.imageRestApi.persistenceEntities.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface ImageRepository extends PagingAndSortingRepository<ImageEntity, Integer> {

    Optional<List<ImageEntity>> findAllByUserEntity(UserEntity userEntity);

    ImageEntity findByImageId(String imageId);

    List<ImageEntity> findAllByTagsEquals(TagEntity tag, Pageable page);

}
