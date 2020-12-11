package com.mimteam.mimclient.models.ws;

import org.jetbrains.annotations.NotNull;

public interface Transferable {
    void fromDataTransferObject(@NotNull MessageDTO dto);
    MessageDTO toDataTransferObject();
}
