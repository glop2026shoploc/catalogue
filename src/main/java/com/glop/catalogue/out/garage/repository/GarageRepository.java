package com.glop.catalogue.out.garage.repository;

import com.glop.catalogue.out.garage.config.GarageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class GarageRepository{
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final GarageProperties props;

    public void upload(String key, InputStream content, long size, String contentType) {
        // En HTTP le SDK signe le contenu et doit le relire : un InputStream ne se lit qu'une fois, on le charge en mémoire
        byte[] bytes;
        try {
            bytes = content.readAllBytes();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        s3Client.putObject(
                r -> r.bucket(props.bucket()).key(key).contentType(contentType),
                RequestBody.fromBytes(bytes));
    }

    public InputStream download(String key) {
        return s3Client.getObject(r -> r.bucket(props.bucket()).key(key));
    }

    public void delete(String key) {
        s3Client.deleteObject(r -> r.bucket(props.bucket()).key(key));
    }

    public URL presignedDownloadUrl(String key, Duration validity) {
        return s3Presigner.presignGetObject(r -> r
                        .signatureDuration(validity)
                        .getObjectRequest(g -> g.bucket(props.bucket()).key(key)))
                .url();
    }

}
