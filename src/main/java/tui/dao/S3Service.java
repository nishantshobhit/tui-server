package tui.dao;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

@Component
public class S3Service {

    private AmazonS3Client client;

    public S3Service() {
        AWSCredentials creds = new AWSCredentials() {
            @Override
            public String getAWSAccessKeyId() {
                return "<SECRET>";
            }

            @Override
            public String getAWSSecretKey() {
                return "<SECRETKEY>";
            }
        };
        client = new AmazonS3Client(creds);
    }

    public String store(InputStream stream, String contentType) {
        String fileName = RandomStringUtils.randomAlphanumeric(8);
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType(contentType);
        client.putObject(new PutObjectRequest("tui.pictures", fileName, stream, meta)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return fileName;
    }
}
