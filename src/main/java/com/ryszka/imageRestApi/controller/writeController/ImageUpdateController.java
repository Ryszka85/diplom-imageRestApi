package com.ryszka.imageRestApi.controller.writeController;

import com.ryszka.imageRestApi.service.dto.ImageDTO;
import com.ryszka.imageRestApi.viewModels.request.SetTagsToImageRequest;
import com.ryszka.imageRestApi.service.serviceV2.writeService.UpdateImageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "image/update")
public class ImageUpdateController {
    private final UpdateImageService updateImageService;

    public ImageUpdateController(UpdateImageService updateImageService) {
        this.updateImageService = updateImageService;
    }

    @PostMapping(value = "/tags")
    public void setImageTags(@RequestBody SetTagsToImageRequest updateRequest) {
        System.out.println(updateRequest);
        updateImageService.setTags(new ImageDTO(updateRequest));
    }

}
