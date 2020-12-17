package com.mimteam.mimclient.models.ws;

import com.mimteam.mimclient.models.dto.MessageDTO;

import org.jetbrains.annotations.NotNull;

public interface Transferable {
    void fromDataTransferObject(@NotNull MessageDTO dto);
    MessageDTO toDataTransferObject();
}
