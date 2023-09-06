package com.oci.poc;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;

@Service
public class FileDownloadService {    

    private String bucketName = "oef_stdf_archive_dev";

    // you can find namespace in your bucket details page under Bucket information in OCI console.
    private String namespaceName = "frvakvxpiiu9";
    
    @Autowired
    private OCIClientConfiguration configuration;

	public void downloadObject(String objectName) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        		.namespaceName(namespaceName)
                .bucketName(bucketName)
                .objectName(objectName)
                .build();

        GetObjectResponse getObjectResponse = configuration.getObjectStorage().getObject(getObjectRequest);

        try (InputStream objectInputStream = getObjectResponse.getInputStream();
             FileOutputStream outputStream = new FileOutputStream("C:/Users/a5143522/GymServices/" + objectName)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = objectInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new IOException("Failed to download object from Object Storage", e);
        }
        
    }
}