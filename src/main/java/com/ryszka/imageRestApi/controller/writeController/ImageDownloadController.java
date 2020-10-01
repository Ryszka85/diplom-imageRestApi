package com.ryszka.imageRestApi.controller.writeController;

import com.ryszka.imageRestApi.service.serviceV2.writeService.FileDownloadService;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("library/download")
public class ImageDownloadController {
    private final FileDownloadService fileService;

    public ImageDownloadController(FileDownloadService fileService) {
        this.fileService = fileService;
    }

    @GetMapping(value = "/file/{name}/user/{userId}")
    public Resource downloadFile(@PathVariable String name,
                                 @PathVariable String userId,
                                 HttpServletResponse httpResp) {
        return fileService.getFileSystem(name, userId, httpResp);
    }
}
