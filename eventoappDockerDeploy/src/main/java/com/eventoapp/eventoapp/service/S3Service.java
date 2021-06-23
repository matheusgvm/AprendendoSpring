package com.eventoapp.eventoapp.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Slf4j
@Service
public class S3Service {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Autowired
    private AmazonS3 amazonS3;

    public String uploadFile(MultipartFile multipartFile) {
        File file = convertMultiPartFileToFile(multipartFile);
        String nomeArquivo = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();

        amazonS3.putObject(new PutObjectRequest(bucketName, nomeArquivo, file));
        file.delete();

        return "Arquivo salvo: " + nomeArquivo;
    }

    public byte[] downloadFile(String nomeArquivo){
        S3Object s3Object = amazonS3.getObject(bucketName, nomeArquivo);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();

        try {
            byte[] content = IOUtils.toByteArray(inputStream);

            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String deleteFile(String nomeArquivo){
        amazonS3.deleteObject(bucketName, nomeArquivo);

        return nomeArquivo + " deletado";
    }

    private File convertMultiPartFileToFile(final MultipartFile multipartFile) {
        File file = new File(multipartFile.getOriginalFilename());
        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (final IOException ex) {
            log.error("Error converting multipartFile to file", ex);
        }
        return file;
    }
}
