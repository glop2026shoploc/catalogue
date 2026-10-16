package com.glop.catalogue.domain.port.out;

import java.io.InputStream;
import java.net.URL;
import java.time.Duration;

public interface FileStorage {
    void upload(String filePath, InputStream content, long size, String contentType);

    URL presignedDownloadUrl(String filePath, Duration urlValidity);
}
