package com.tlc.chatgptweb.service;

import com.tlc.chatgptweb.dto.Message;
import com.tlc.chatgptweb.enums.Dialog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheService {

    @Cacheable(cacheNames = "openai-message", key = "#id")
    public List<Message> messageCacheable(String id) {
        log.info("Cacheable : [{}]", id);
        return new ArrayList<>(Dialog.getInitializeDialog());
    }

    @CachePut(cacheNames = "openai-message" , key = "#id")
    public List<Message> messageCachePut(String id, List<Message> cachedMessages) {
        log.info("CachePut : [{}], cachedMessages count : [{}]", id, cachedMessages.size());
        return cachedMessages;
    }

    @CacheEvict(cacheNames = "openai-message", key = "#id")
    public void messageCacheEvict(String id) {
        log.info("CacheEvict : [{}]", id);
    }

}
