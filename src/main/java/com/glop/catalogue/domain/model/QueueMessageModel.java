package com.glop.catalogue.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class QueueMessageModel {
    String message;
    String object;
    List<String> attachments;
}
