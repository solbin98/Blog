package com.project.util.data;

import org.springframework.web.multipart.MultipartFile;

public class ImageDto {
    private MultipartFile[] image;

    public MultipartFile[] getImage() {return image;}

    public void setImage(MultipartFile[] image) {this.image = image;}
}
