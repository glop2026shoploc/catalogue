package com.glop.catalogue.in.restapi.mapper;

import com.glop.catalogue.domain.model.QueueMessageModel;
import com.glop.catalogue.in.restapi.payload.QueueMessageSend;
import com.glop.catalogue.transversal.model.QueueMessageTransversalModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QueueInMapper {
    @Mapping(target = "message", source = "message")
    @Mapping(target = "object", source = "object")
    @Mapping(target = "attachments", source = "attachments")
    QueueMessageModel toQueueMessageModel(QueueMessageSend messageSend);

}
