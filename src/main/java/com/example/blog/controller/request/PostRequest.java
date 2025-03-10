package com.example.blog.controller.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PostRequest {
    public String title;
    public String content;
    public String tags;
    public MultipartFile image;
}
