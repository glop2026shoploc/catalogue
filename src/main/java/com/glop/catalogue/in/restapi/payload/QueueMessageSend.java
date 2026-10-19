package com.glop.catalogue.in.restapi.payload;

import lombok.Data;

import java.util.List;

@Data
public class QueueMessageSend {
    String message;
    String object;
    List<String> attachments;
}
