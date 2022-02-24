package com.example.tiktokproject.controller;

import com.example.tiktokproject.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/files/{filename}")
    public ResponseEntity<OutputStream> download(@PathVariable String filename){
        return new ResponseEntity<>(fileService.download(filename), HttpStatus.OK);
    }
}
