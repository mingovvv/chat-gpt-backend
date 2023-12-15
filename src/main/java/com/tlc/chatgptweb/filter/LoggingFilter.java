package com.tlc.chatgptweb.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingFilter implements WebFilter {

    @Override
    @CrossOrigin
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest req = exchange.getRequest();
        log.info("req = [{}], param = {}", req.getURI(), req.getQueryParams());
        return chain.filter(exchange)
                .doOnSuccess((aVoid) -> log.info("res = [{}], statusCode = {}", req.getURI(), exchange.getResponse().getStatusCode()))
                .doOnError((throwable) -> log.info("res = [{}], statusCode = {}, e = {}", req.getURI(), exchange.getResponse().getStatusCode(), throwable.getMessage()));

    }

}