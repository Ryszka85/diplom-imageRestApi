package com.ryszka.imageRestApi.controller.readController;

import com.ryszka.imageRestApi.service.dto.TagDTO;
import com.ryszka.imageRestApi.service.serviceV2.readService.ReadTagsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tags")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080", "http://178.112.217.30:8080"}, methods = {RequestMethod.GET, RequestMethod.POST})
public class ReadTagsController {
    private final ReadTagsService tagService;

    public ReadTagsController(ReadTagsService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(value = "oida")
    public String foo() {
        return "OIDAAAA!!";
    }

    @GetMapping(path = "search/{term}")
    public List<TagDTO> getTagsLikeTerm(@PathVariable String term) {
        return tagService.getTagsLikeSearchTerm(term);
    }


}
