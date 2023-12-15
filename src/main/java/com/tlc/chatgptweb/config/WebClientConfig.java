package com.tlc.chatgptweb.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient openaiClient(@Value("${openai.url}") String openaiUrl,
                                  @Value("${openai.api-key}") String openaiKey,
                                  @Value("${openai.connection-timeout}") Duration connectionTimeout,
                                  @Value("${openai.write-timeout}") Duration writeTimeout,
                                  @Value("${openai.read-timeout}") Duration readTimeout) {

        log.info("openai.url : [{}], openai.api-key : [{}]", openaiUrl, openaiKey);

        return WebClient
                .builder()
                .exchangeStrategies(defaultExchangeStrategies())
                .baseUrl(openaiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openaiKey)
                .clientConnector(new ReactorClientHttpConnector(httpClient(connectionTimeout, readTimeout, writeTimeout)))
                .filter(logRequest())
                .filter(logResponse())
                .build();

    }

    public HttpClient httpClient(Duration connectionTimeout, Duration readTimeout, Duration writeTimeout) {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout.toMillisPart())
                .responseTimeout(readTimeout)
                .doOnConnected(connection -> connection.addHandlerLast(new ReadTimeoutHandler(readTimeout.toMillis(), TimeUnit.MILLISECONDS))) // 읽기 타임아웃 설정
                .doOnConnected(connection -> connection.addHandlerLast(new WriteTimeoutHandler(writeTimeout.toMillis(), TimeUnit.MILLISECONDS))); // 쓰기 타임아웃 설정
    }

    public ConnectionProvider connectionProvider() {
        return ConnectionProvider.builder("http-pool")
                .maxConnections(100) // connection pool
                .pendingAcquireTimeout(Duration.ofMillis(0)) // connection pool 대기시간
                .pendingAcquireMaxCount(-1) // connection pool에서 conn 재시도 (-1: no limit)
                .maxIdleTime(Duration.ofMillis(2000L)) // connection pool에서 idle 상태 유지시간
                .build();
    }

    public ExchangeStrategies defaultExchangeStrategies() {

        ObjectMapper om = new ObjectMapper();

        ExchangeStrategies exchangeStrategies = ExchangeStrategies
                .builder()
                .codecs(configurer -> {
                    configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(om, MediaType.APPLICATION_JSON));
                    configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(om, MediaType.APPLICATION_JSON));
                    configurer.defaultCodecs().maxInMemorySize(1024 * 1024); // in-memory buffer default : 256KB -> 1048KB 변경
                }).build();

        exchangeStrategies
                .messageWriters().stream()
                .filter(LoggingCodecSupport.class::isInstance)
                .forEach(writer -> ((LoggingCodecSupport) writer).setEnableLoggingRequestDetails(true)); // logging

        return exchangeStrategies;
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("request url : {},  method : {}", clientRequest.url(), clientRequest.method());
            clientRequest.headers().forEach((name, values) ->
                    values.forEach(value -> log.info("request header key : {}, value : {}", name, value)));
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            clientResponse.headers().asHttpHeaders().forEach((name, values) ->
                    values.forEach(value -> log.info("response header key : {}, value : {}", name, value)));
            return Mono.just(clientResponse);
        });
    }

}