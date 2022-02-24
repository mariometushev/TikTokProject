package com.example.tiktokproject.services;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

@Service
public class FileService {

    public void download(String filename, HttpServletResponse response) {
        File f = new File("photos" + File.separator + filename);
        try {
            Files.copy(f.toPath(), response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
