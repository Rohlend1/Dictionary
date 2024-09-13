package com.example.dictionary.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.example.dictionary.dict.finder.FindExamplesServiceGrpc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Log4j2
public class GrpcClient {
    private FindExamplesServiceGrpc.FindExamplesServiceBlockingStub stub;
    @Value("${grpc.server.host}")
    private String host;
    @Value("${grpc.server.port}")
    private int port;

    @PostConstruct
    public void init(){
        log.info(host);
        log.info(port);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        stub = FindExamplesServiceGrpc.newBlockingStub(channel);
    }
}
