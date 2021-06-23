package com.eventoapp.eventoapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.eventoapp.eventoapp.service.S3Service;

@RestController
@RequestMapping("/file")
public class StorageController {

    @Autowired
    private S3Service service;

    @PostMapping("/upload")
    public ResponseEntity<String> salvarArquivo(@RequestParam(value = "file") MultipartFile file){

        return  new ResponseEntity<>(service.uploadFile(file), HttpStatus.OK);
    }

    @GetMapping("/download/{nomeArquivo}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String nomeArquivo){
        byte[] data = service.downloadFile(nomeArquivo);
        ByteArrayResource resource =  new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + nomeArquivo + "\"")
                .body(resource);

    }
    @DeleteMapping("/delete/{nomeArquivo}")
    public ResponseEntity<String> deleteFile(@PathVariable String nomeArquivo){
        return new ResponseEntity<>(service.deleteFile(nomeArquivo), HttpStatus.OK);
    }

}
