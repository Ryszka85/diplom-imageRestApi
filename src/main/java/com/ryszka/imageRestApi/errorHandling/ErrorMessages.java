package com.ryszka.imageRestApi.errorHandling;

public enum ErrorMessages {
    IMAGES_NOT_FOUND_BY_TAG("Unable to find images by given tag."),
    NOT_FOUND_BY_EID("Unable to find entity by given entityId :"),
    NOT_FOUND_BY_USERNAME("Unable to find entity by given user name :"),
    NOT_FOUND_BY_SESSIONID("Unable to find entity by given sessionId :"),
    LOGIN_FAIL("Failed to login user!"),
    USER_ALREADY_EXISTS("User already exists."),
    USER_SIGNUP_FAILED("Failed to create new user!"),
    SAVE_TO_DB_ERROR("Failed to save entity to db."),
    CONNECTION_ERROR("Failed to establish connection to resource."),
    SAVE_TO_FTP_ERROR("Failed to save entity to FTP"),
    ROLLED_BACK_IN_TX("Rolled back because error occurred."),
    INVALID_ARGUMENTS("Provided arguments are not valid."),
    ADDRESS_NOT_FOUND("Address could not be found!");

    private String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
