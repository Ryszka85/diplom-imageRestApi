package com.ryszka.imageRestApi.controller.writeController;

import com.ryszka.imageRestApi.service.serviceV2.writeService.FileDownloadService;
import com.ryszka.imageRestApi.service.serviceV2.writeService.AddToUserLibraryService;
import com.ryszka.imageRestApi.service.dto.ImageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("images")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080", "http://178.112.217.30:8080"}, methods = {RequestMethod.GET, RequestMethod.POST})
public class UserLibraryStorageController {
    private final AddToUserLibraryService libraryStorageService;
    private final FileDownloadService fileService;
    private Logger logger = LoggerFactory.getLogger(UserLibraryStorageController.class);

    public UserLibraryStorageController(AddToUserLibraryService imageWriteRequestService, FileDownloadService fileService) {
        this.fileService = fileService;
        this.libraryStorageService = imageWriteRequestService;
    }

    @PostMapping(path = "insert/{userId}", consumes = {"multipart/form-data"})
    public void addImageToUserLibrary(@PathVariable String userId,
                                      @RequestParam("file") MultipartFile file) throws IOException {
        try {
            this.libraryStorageService.addImageToUserLibrary(new ImageDTO(userId, file));
        } catch (Exception e) {
            logger.error("Fucking fuck fuck");
        }
    }

}
