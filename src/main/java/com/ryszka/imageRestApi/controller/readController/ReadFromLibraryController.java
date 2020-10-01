package com.ryszka.imageRestApi.controller.readController;

import com.ryszka.imageRestApi.errorHandling.EntityNotFoundException;
import com.ryszka.imageRestApi.errorHandling.ErrorMessages;
import com.ryszka.imageRestApi.viewModels.response.UserImageResponseModel;
import com.ryszka.imageRestApi.service.serviceV2.readService.ReadUserLibraryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("library/search-by")
public class ReadFromLibraryController {
    private final ReadUserLibraryService readService;

    public ReadFromLibraryController(ReadUserLibraryService readService) {
        this.readService = readService;
    }

    @GetMapping(value = "user/{userId}")
    public ResponseEntity<Optional<List<UserImageResponseModel>>> getImagesFromUserGallery(@PathVariable String userId) {
        Optional<List<UserImageResponseModel>> output = readService.getUserImages(userId);
        return validateQueryAndGetResponse(output,
                ErrorMessages.NOT_FOUND_BY_EID);
    }

    @GetMapping(value = "tag/{tag}")
    public ResponseEntity<Optional<List<UserImageResponseModel>>> test(@PathVariable String tag) {
        Optional<List<UserImageResponseModel>> output = this.readService
                .getImagesByTagNamePageable(tag, 0);
        return validateQueryAndGetResponse(output,
                ErrorMessages.IMAGES_NOT_FOUND_BY_TAG);
    }

    public static ResponseEntity<Optional<List<UserImageResponseModel>>> validateQueryAndGetResponse(
            Optional<List<UserImageResponseModel>> userImagesOpt,
            ErrorMessages error) {
        if (userImagesOpt.isPresent())
            return ResponseEntity
                    .ok(userImagesOpt);
        throw new EntityNotFoundException(error.getMessage());
    }
}
