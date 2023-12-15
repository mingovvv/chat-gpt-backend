package com.tlc.chatgptweb.service;

import com.tlc.chatgptweb.dto.Message;
import com.tlc.chatgptweb.dto.OpenaiRequest;
import com.tlc.chatgptweb.dto.OpenaiStreamResponse;
import com.tlc.chatgptweb.dto.client.MessageRequest;
import com.tlc.chatgptweb.dto.client.MessageStreamResponse;
import com.tlc.chatgptweb.enums.Role;
import com.tlc.chatgptweb.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final WebClient openaiClient;
    private final CacheService cacheService;

    public Flux<MessageStreamResponse> reactiveMessageCall(MessageRequest messageRequest) {

        StringBuilder assistanceAnswer = new StringBuilder();

        List<Message> cachedMessages = cacheService.messageCacheable(messageRequest.id());
        cachedMessages.add(Message.of(Role.USER, messageRequest));

        return openaiClient
                .post()
                .uri("/v1/chat/completions")
                .bodyValue(OpenaiRequest.from(cachedMessages, true))
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), clientResponse -> clientResponse.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToFlux(String.class)
                .takeWhile(text -> !text.equals("[DONE]"))
                .map(data -> MessageStreamResponse.from(JsonUtil.jsonToObject(data, OpenaiStreamResponse.class)))
                .doOnNext(streamData -> {
                    if (!streamData.isFinished()) assistanceAnswer.append(streamData.message());
                })
                .doAfterTerminate(() -> {
                    log.info("user id : [{}], user question : [{}]", messageRequest.id(), messageRequest.message());
                    log.info("openai answer : [{}]", assistanceAnswer);
                    cachedMessages.add(Message.of(Role.ASSISTANT, assistanceAnswer.toString()));
                    // 문답 캐싱
                    cacheService.messageCachePut(messageRequest.id(), cachedMessages);
                });
    }

}


