package com.example.tiktokproject.services;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

@Service
public class FileService {
    public OutputStream download(String filename) {
        File f = new File(filename);
        try {
           return Files.newOutputStream(f.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
