package com.ryszka.imageRestApi.viewModels.response;

import com.ryszka.imageRestApi.service.dto.TagDTO;

import java.io.Serializable;
import java.util.List;

public class UserImageResponseModel implements Serializable {
    private static final long serialVersionUID = 3148099477137315634L;
    private String name, link, imageId;
    private UserDetailsResponseModel user;
    private List<TagDTO> tags;
    private List<UserImageResponseModel> likes;

    public UserImageResponseModel(String name, String link, String imageId,
                                  UserDetailsResponseModel user, List<TagDTO> tags,
                                  List<UserImageResponseModel> likes) {
        this.name = name;
        this.link = link;
        this.imageId = imageId;
        this.user = user;
        this.tags = tags;
        this.likes = likes;
    }

    public UserImageResponseModel(String name, String link, String imageId,
                                  UserDetailsResponseModel user, List<TagDTO> tags) {
        this.name = name;
        this.link = link;
        this.imageId = imageId;
        this.user = user;
        this.tags = tags;
    }

    public List<UserImageResponseModel> getLikes() {
        return likes;
    }

    public void setLikes(List<UserImageResponseModel> likes) {
        this.likes = likes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public UserDetailsResponseModel getUser() {
        return user;
    }

    public void setUser(UserDetailsResponseModel user) {
        this.user = user;
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }
}
