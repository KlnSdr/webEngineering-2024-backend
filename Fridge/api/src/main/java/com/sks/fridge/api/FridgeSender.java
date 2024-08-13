package com.sks.fridge.api;

import com.sks.base.api.BaseSender;

public interface FridgeSender extends BaseSender<FridgeRequestMessage, FridgeResponseMessage> {
    FridgeResponseMessage sendRequest(FridgeRequestMessage message);
    void sendResponse(FridgeRequestMessage request, FridgeResponseMessage response);
}
