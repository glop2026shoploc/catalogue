package com.glop.catalogue.in.rabbitmq.mapper;

import com.glop.catalogue.domain.model.QueueMessageModel;
import com.glop.catalogue.in.restapi.payload.QueueMessageSend;
import com.glop.catalogue.transversal.model.QueueMessageTransversalModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RabbitMqMapper {
    @Mapping(target = "message", source = "message")
    QueueMessageModel toQueueMessageModel(QueueMessageTransversalModel transversalModel);
}
