package com.ryszka.imageRestApi.service.serviceV2.writeService;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.ryszka.imageRestApi.config.FireBaseStorageConfig;
import com.ryszka.imageRestApi.dao.ImageDAO;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.persistenceEntities.FtpPersistenceEntity;
import com.ryszka.imageRestApi.security.AppConfigProperties;
import com.ryszka.imageRestApi.service.dto.ImageDTO;
import com.ryszka.imageRestApi.repository.ImageRepository;
import com.ryszka.imageRestApi.util.mapper.ObjectMapper;
import com.ryszka.imageRestApi.util.mapper.mapStrategies.ImageDtoToImageEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;

@Service
public class DatabaseAndFTPStorageService {
    private final Logger logger = LoggerFactory.getLogger(DatabaseAndFTPStorageService.class);
    private final ImageRepository imageRepository;
    private final TransactionTemplate transactionTemplate;
    private final ImageDAO imageDAO;
    private final FireBaseStorageConfig storageConfig;

    public DatabaseAndFTPStorageService(ImageRepository imageRepository, TransactionTemplate transactionTemplate, ImageDAO imageDAO, FireBaseStorageConfig storageConfig) {
        this.imageRepository = imageRepository;
        this.transactionTemplate = transactionTemplate;
        this.imageDAO = imageDAO;
        this.storageConfig = storageConfig;
    }

    /*public DatabaseAndFTPStorageService(ImageRepository imageRepository,
                                        TransactionTemplate transactionTemplate,
                                        ImageDAO imageDAO) {
        this.imageRepository = imageRepository;
        this.transactionTemplate = transactionTemplate;
        this.imageDAO = imageDAO;
    }*/

    public void storeToDbAndFTPInTransaction(ImageDTO imageDTO) {
        transactionTemplate.execute(savFile(imageDTO));
    }

    public TransactionCallback<Boolean> savFile(ImageDTO imageDTO) {
        return transactionStatus -> {
            FtpStorageService storageService = new FtpStorageService();
            boolean storeFtpStatus = false;
            boolean dbStoreStatus = false;
            try {
                /*storeFtpStatus = storageService.makeDirAndSaveFileToFileSystem(
                        new FtpPersistenceEntity(imageDTO.getPath() + "/" + imageDTO.getName(), imageDTO.getInputStream())
                );*/
                storageConfig.initFireBaseStorage(storage -> {
                    BlobId blobId = BlobId.of(AppConfigProperties.BUCKET_NAME, imageDTO.getPath() + "/" + imageDTO.getName());
                    BlobInfo info = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();
                    storage.create(info, imageDTO.getContent());
                });
                dbStoreStatus = imageDAO.saveImage(ObjectMapper.mapByStrategy(
                        imageDTO, new ImageDtoToImageEntity()));
            } catch (Exception e) {
                logger.error(e.getMessage());
                transactionStatus.setRollbackOnly();
                logger.error(ErrorMessages.ROLLED_BACK_IN_TX.getMessage());
            }
            return storeFtpStatus && dbStoreStatus;
        };
    }
}
