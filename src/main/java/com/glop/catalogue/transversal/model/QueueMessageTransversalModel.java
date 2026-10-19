package com.glop.catalogue.transversal.model;

import lombok.Data;

import java.util.List;

@Data
public class QueueMessageTransversalModel {
    String message;
    String object;
    List<String> attachments;
}
