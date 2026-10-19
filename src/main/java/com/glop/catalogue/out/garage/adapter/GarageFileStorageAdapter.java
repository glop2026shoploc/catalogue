package com.glop.catalogue.out.garage.adapter;

import com.glop.catalogue.domain.port.out.FileStorage;
import com.glop.catalogue.out.garage.repository.GarageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class GarageFileStorageAdapter implements FileStorage {
    private final GarageRepository garageRepository;

    @Override
    public void upload(String filePath, InputStream content, long size, String contentType) {
        garageRepository.upload(filePath,content,size,contentType);
    }

    @Override
    public URL presignedDownloadUrl(String filePath, Duration urlValidity) {
        return garageRepository.presignedDownloadUrl(filePath,urlValidity);
    }
}
