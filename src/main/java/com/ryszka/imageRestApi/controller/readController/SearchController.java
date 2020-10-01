package com.ryszka.imageRestApi.controller.readController;

import com.ryszka.imageRestApi.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

@RestController
@RequestMapping(value = "query")
public class SearchController {
    private SearchService searchService;
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }


    @GetMapping(value = "{pMethod}/{params}")
    public ResponseEntity<Object> getImagesFromUserGallery(@PathVariable String[] params,
                                                           @PathVariable String pMethod) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        Class<String>[] argsClass = new Class[params.length];
        Arrays.fill(argsClass, String.class);
        Method method = SearchService.class.getDeclaredMethod(pMethod, argsClass);
        return ResponseEntity.ok(method.invoke(searchService, params));
    }
}
