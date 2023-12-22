package com.tlc.chatgptweb.controller;

import com.tlc.chatgptweb.dto.Message;
import com.tlc.chatgptweb.dto.client.MessageRequest;
import com.tlc.chatgptweb.dto.client.MessageStreamResponse;
import com.tlc.chatgptweb.service.CacheService;
import com.tlc.chatgptweb.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final CacheService cacheService;

    @CrossOrigin
    @GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MessageStreamResponse> reactiveMessageCall(@RequestParam String id, @RequestParam String message) {
        log.info("/reactive-message = {}, {}", id, message);
        return messageService.reactiveMessageCall(new MessageRequest(id, message));
    }

    @CrossOrigin
    @GetMapping(value = "/messages")
    public List<Message> getMessages(@RequestParam("id") String id) {
        return cacheService.messageCacheable(id);
    }

    @CrossOrigin
    @DeleteMapping(value = "/messages")
    public void deleteMessages(@RequestParam("id") String id) {
        cacheService.messageCacheEvict(id);
    }

}