package com.glop.catalogue.domain.service;

import com.glop.catalogue.domain.port.in.ImagePortIn;
import com.glop.catalogue.domain.port.out.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService implements ImagePortIn {

    private static final String PREFIX = "images/";
    private static final Duration URL_VALIDITY = Duration.ofMinutes(10);
    private static final Map<String, String> EXTENSIONS = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp");

    private final FileStorage fileStorage;

    @Override
    public String upload(InputStream content, long size, String contentType) {
        String extension = EXTENSIONS.get(contentType);
        if (extension == null) {
            throw new IllegalArgumentException("Type de fichier non supporté : " + contentType);
        }
        String imageId = UUID.randomUUID() + extension;
        fileStorage.upload(PREFIX + imageId, content, size, contentType);
        return imageId;
    }

    @Override
    public URL getDownloadUrl(String imageId) {
        return fileStorage.presignedDownloadUrl(PREFIX + imageId, URL_VALIDITY);
    }
}