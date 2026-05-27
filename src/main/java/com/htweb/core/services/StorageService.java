package com.htweb.core.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {

    String uploadImage(MultipartFile file) throws IOException;

    String uploadPdf(MultipartFile file, String targetFolder) throws IOException;
}