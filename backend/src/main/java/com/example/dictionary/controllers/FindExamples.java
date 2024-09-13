package com.example.dictionary.controllers;

import com.example.dictionary.grpc.GrpcClient;
import com.example.dictionary.services.TextFormatter;
import lombok.extern.log4j.Log4j2;
import org.example.dictionary.dict.finder.DocumentInfo;
import org.example.dictionary.dict.finder.FindExampleResponse;
import org.example.dictionary.dict.finder.FindExamplesRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/find")
@Log4j2
public class FindExamples {

    private GrpcClient grpc;

    @GetMapping("/{word}")
    public List<String> findByWord(@PathVariable("word") String word){
        FindExamplesRequest request = FindExamplesRequest.newBuilder().setText(word).build();

        FindExampleResponse response = grpc.getStub().findExamples(request);

        return response.getDocumentsList().stream()
                .map(document -> TextFormatter.formatFtsResponse(document.getText(), document.getMainWord()))
                .toList();
        //ToDO починить хуйню, не работает т.к. долбоебы не догадались переопределить метод toString() для целого объекта,
        // а переопределили только для get методов toStringUTF8(), см внутри класса DocumentInfo
    }

}
