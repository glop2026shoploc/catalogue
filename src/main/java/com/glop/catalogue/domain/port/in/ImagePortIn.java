package com.glop.catalogue.domain.port.in;

import java.io.InputStream;
import java.net.URL;

public interface ImagePortIn {
    String upload(InputStream content, long size, String contentType);
    URL getDownloadUrl(String imageId);
}