package com.sks.gateway.demo;

import com.sks.demo.api.DemoRequestMessage;
import com.sks.demo.api.DemoResponseMessage;
import com.sks.demo.api.DemoSender;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/demo")
public class DemoResource {
    private final DemoSender sender;

    public DemoResource(DemoSender sender) {
        this.sender = sender;
    }

    @GetMapping(value = "/send", produces = MediaType.APPLICATION_JSON_VALUE)
    public String send() {
        final DemoResponseMessage response = sender.sendRequest(new DemoRequestMessage ("Hello from gateway"));
        return response.getMessage();
    }

    @PostMapping(value = "/send", produces = MediaType.APPLICATION_JSON_VALUE)
    public DemoResponseMessage send(@RequestBody DemoRequestBody body) {
        return sender.sendRequest(new DemoRequestMessage(body.getMessage()));
    }
}
