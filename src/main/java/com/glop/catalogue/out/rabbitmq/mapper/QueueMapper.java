package com.glop.catalogue.out.rabbitmq.mapper;

import com.glop.catalogue.domain.model.QueueMessageModel;
import com.glop.catalogue.transversal.model.QueueMessageTransversalModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QueueMapper {
    @Mapping(target = "message", source = "message")
    @Mapping(target = "attachments", source = "attachments")
    @Mapping(target = "object", source = "object")
    QueueMessageTransversalModel toQueueMessage(QueueMessageModel messageModel);
}
