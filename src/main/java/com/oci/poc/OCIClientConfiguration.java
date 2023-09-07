package com.oci.poc;

import java.io.IOException;
import org.springframework.context.annotation.Configuration;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;

@Configuration
public class OCIClientConfiguration {
    
    // Path to OCI config file
    String configurationFilePath = "./src/main/resources/config";
    String profile = "DEFAULT";

    
    public ObjectStorage getObjectStorage() throws IOException{
                //load config file

        final ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(configurationFilePath, profile);

        final ConfigFileAuthenticationDetailsProvider provider =
            new ConfigFileAuthenticationDetailsProvider(configFile);

        //build and return client

        return ObjectStorageClient.builder()
        .build(provider);
        }
    
}
