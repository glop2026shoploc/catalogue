package com.glop.catalogue.in.restapi.payload;

import lombok.Data;

@Data
public class TestPayload {
    String testMessage1;
    String testMessage2;
    int numberOfTestRequestSinceBeginning;
}
