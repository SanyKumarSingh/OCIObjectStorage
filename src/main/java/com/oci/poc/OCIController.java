package com.oci.poc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping
public class OCIController {
	
	private final String urlPrefix = "https://console.eu-frankfurt-1.oraclecloud.com/object-storage/buckets/"; 
    
    @Autowired
    private FileUploadService fileUploadService;
    
    @Autowired
    private FileDownloadService fileDownloadService;

    @PostMapping(path = "upload")
    public ResponseEntity<Object> uploadFile(@RequestParam("file")MultipartFile file){

        try {
            fileUploadService.upload(file);
            return ResponseEntity.ok().body("Uploaded File : "+file.getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(path = "file/{fileName}")
    public ResponseEntity<Object> getURl(@PathVariable(value = "fileName") String fileName){

        try {
           String accessUri = fileUploadService.getFileObject(fileName).getPreauthenticatedRequest().getAccessUri();
            return ResponseEntity.ok().body(urlPrefix + accessUri);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }
    
    @PostMapping(path = "download")
    public ResponseEntity<Object> downloadFile(@RequestParam("file")String objectName){

        try {
        	fileDownloadService.downloadObject(objectName);
            return ResponseEntity.ok().body("Downloaded File : "+objectName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
