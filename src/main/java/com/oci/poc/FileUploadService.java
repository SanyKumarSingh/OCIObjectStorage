package com.oci.poc;

import java.io.InputStream;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.oracle.bmc.objectstorage.model.CreatePreauthenticatedRequestDetails;
import com.oracle.bmc.objectstorage.model.PreauthenticatedRequest.BucketListingAction;
import com.oracle.bmc.objectstorage.requests.CreatePreauthenticatedRequestRequest;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.CreatePreauthenticatedRequestResponse;

@Service
public class FileUploadService {    

	@Value("${oci.objectstorage.bucket-name}")
    private String bucketName;

	@Value("${oci.objectstorage.namespaceName}")
    private String namespaceName;
    
    @Autowired
    private OCIClientConfiguration configuration;

    public void upload(MultipartFile file) throws Exception {

        String objectName = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();


        //build upload request

        PutObjectRequest putObjectRequest =
                PutObjectRequest.builder()
                        .namespaceName(namespaceName)
                        .bucketName(bucketName)
                        .objectName(objectName)
                        .contentLength(file.getSize())
                        .putObjectBody(inputStream)
                        .build();
    
        //upload the file
        try {
            configuration.getObjectStorage().putObject(putObjectRequest);        
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally{
            configuration.getObjectStorage().close();
        }    
    }

    public CreatePreauthenticatedRequestResponse getFileObject(String objectName) throws Exception{

        // Build request details
        CreatePreauthenticatedRequestDetails createPreauthenticatedRequestDetails = CreatePreauthenticatedRequestDetails.builder()
            .name("OCI_Request")
            .bucketListingAction(BucketListingAction.Deny)
            .objectName(objectName)
            //readonly access
            .accessType(CreatePreauthenticatedRequestDetails.AccessType.ObjectRead)
            //here we set expiration time as 1 hour
            .timeExpires(Date.from(Instant.now().plusSeconds(3600))).build();

        //Build request
        CreatePreauthenticatedRequestRequest createPreauthenticatedRequestRequest = CreatePreauthenticatedRequestRequest.builder()
            .namespaceName(namespaceName)
            .bucketName(bucketName)
            .createPreauthenticatedRequestDetails(createPreauthenticatedRequestDetails)
            .opcClientRequestId(UUID.randomUUID().toString()).build();

            // send request to oci
        CreatePreauthenticatedRequestResponse response = configuration.getObjectStorage().createPreauthenticatedRequest(createPreauthenticatedRequestRequest);
        configuration.getObjectStorage().close();
        return response;
    }

}