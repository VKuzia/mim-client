package com.mimteam.mimclient.models.ws;

import com.mimteam.mimclient.models.dto.MessageDto;

import org.jetbrains.annotations.NotNull;

public interface Transferable {
    void fromDataTransferObject(@NotNull MessageDto dto);
    MessageDto toDataTransferObject();
}
