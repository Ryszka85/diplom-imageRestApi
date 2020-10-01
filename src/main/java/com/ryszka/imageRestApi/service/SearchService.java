package com.ryszka.imageRestApi.service;

import com.ryszka.imageRestApi.service.dto.TagDTO;
import com.ryszka.imageRestApi.service.serviceV2.readService.ReadTagsService;
import com.ryszka.imageRestApi.util.mapper.dbMappers.ZipCodesAndCitiesByRegion;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {
    public final ReadTagsService readTagsService;
    public final AddressService addressService;

    public SearchService(ReadTagsService readTagsService, AddressService addressService) {
        this.readTagsService = readTagsService;
        this.addressService = addressService;
    }

    public List<ZipCodesAndCitiesByRegion> searchByZipAndRegion(String region, String zip) {
        return addressService.validateAddress(zip, region).join();
    }

    public List<TagDTO> searchByTag(String tag) {
        return readTagsService.getTagsLikeSearchTerm(tag);
    }
}
