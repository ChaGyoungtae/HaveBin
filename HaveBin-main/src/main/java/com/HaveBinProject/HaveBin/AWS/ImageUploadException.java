package com.HaveBinProject.HaveBin.AWS;

public class ImageUploadException extends RuntimeException {
    public ImageUploadException() {
        super("Image upload failed");
    }

    public ImageUploadException(String message) {
        super(message);
    }
}
