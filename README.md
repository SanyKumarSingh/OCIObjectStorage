# OCIObjectStorage
Connect to the Oracle Cloud Infrastructure Object Storage and Upload/Download objects in various storage tiers. 

pom.xml has the oci.sdk dependency required to interact with OCI object storage from java/spring boot

OCIClientConfiguration.java file reads the OCI configuration kept in the config file kept at root level in the Project.
### The config is generated following below steps.
Login to your Console view - https://console.eu-frankfurt-1.oraclecloud.com/
Go To Storage → Buckets → Choose a bucket example oef_stdf_archive_dev → Here we can Upload/Download. 
Navigate to your profile → Go to API keys → Add Keys → For 1st time users download the public and the private keys.
Note down the credentials shared in the dialog box.

[DEFAULT]
user=ocid1.user.oc1..aaaaaaaajqbdfzwvhjlparvhb6en6odklomewmp46ikzg3okb53htz55nlxa
fingerprint=b1:04:cb:da:61:81:31:32:6d:4e:d1:ee:fc:4b:e5:4a
tenancy=ocid1.tenancy.oc1..aaaaaaaanmey77kpzhv2glqt45yvxxhn2uzqfsvakreckzhwrwxpzzh7tbca
region=eu-frankfurt-1
key_file=<path to your private keyfile> # TODO

FileUploadService.Java and FileDownloadService.Java are the services that caters to file upload and download.

Application.Java is the main class to start the application.

Post Request for File Upload - http://DEU-5CG2232PP1.adwin.renesas.com:8080/upload
Go to Body, form-data, Choose File, then provide key=file and value is upload any file to upload.
Response : Http status 200 and body Uploaded File : GMANUF-SpringBootApplication-110823-0834-312.pdf

Post Request for File Download - http://DEU-5CG2232PP1.adwin.renesas.com:8080/download
Go to Body, form-data, Choose text, then key=file and value=GMANUF-SpringBootApplication-110823-0834-312.pdf
Response : Http status 200 and body Downloaded File : GMANUF-SpringBootApplication-110823-0834-312.pdf
Check the path "C:/Users/a5143522/GymServices/" for the file.


