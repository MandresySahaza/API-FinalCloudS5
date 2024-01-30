package com.pkg.occasion.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.web.multipart.MultipartFile;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;

public class Util {
    public static String uploadFile(MultipartFile file) throws Exception{
        if(FirebaseApp.getApps().isEmpty()){
            File f = new File("final-cloud-9c2fc-firebase-adminsdk-9dhq1-2ff5eb3341.json");

            InputStream serviceAccount = new FileInputStream(f);
            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("final-cloud-9c2fc.appspot.com")
                .build();

            FirebaseApp.initializeApp(options);
        }

        
        // File file = new File("firebase/love.jpg");

        StorageClient storageClient = StorageClient.getInstance();

        String extension = file.getOriginalFilename().split("\\.")[1];

        // String extension = file.getName().split("\\.")[1];


        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();


        Path tempFile = convFile.toPath();
        String fileName = UUID.randomUUID().toString() + "." + extension;

        String contentType = Files.probeContentType(tempFile);

        Blob b = storageClient.bucket().create(fileName, Files.readAllBytes(tempFile), contentType);

        convFile.delete();

        String downloadUrl = storageClient.bucket().get(b.getBlobId().getName()).signUrl(1, TimeUnit.DAYS).toString();
        return downloadUrl;
    }

}
