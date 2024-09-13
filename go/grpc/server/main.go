package main

import (
	"bytes"
	"context"
	"encoding/json"
	"github.com/elastic/go-elasticsearch/v8"
	"github.com/joho/godotenv"
	go_grpc_server "go.grpc.server/gen"
	"google.golang.org/grpc"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
	"log"
	"net"
	"strconv"
)

const port = 1111

type findExampleServer struct {
	esClient *elasticsearch.Client
	go_grpc_server.UnimplementedFindExamplesServiceServer
}

func (s *findExampleServer) mustEmbedUnimplementedFindExamplesServiceServer() {
	panic("implement me")
}

func (s *findExampleServer) FindExamples(ctx context.Context, req *go_grpc_server.FindExamplesRequest) (*go_grpc_server.FindExampleResponse, error) {
	log.Printf("Received request: %s", req.Text)

	// Проверка на null значение
	if req.Text == "" {
		return nil, status.Errorf(codes.InvalidArgument, "Word cannot be null")
	}

	// Создание запроса для поиска в Elasticsearch
	query := map[string]interface{}{
		"query": map[string]interface{}{
			"match": map[string]interface{}{
				"text": req.Text, // Искать по полю "text"
			},
		},
	}

	var buf bytes.Buffer
	if err := json.NewEncoder(&buf).Encode(query); err != nil {
		return nil, status.Errorf(codes.Internal, "Failed to encode query: %v", err)
	}

	res, err := s.esClient.Search(
		s.esClient.Search.WithContext(ctx),
		s.esClient.Search.WithIndex("test2"), // Замените на имя вашего индекса
		s.esClient.Search.WithBody(&buf),
		s.esClient.Search.WithTrackTotalHits(true),
	)
	if err != nil {
		return nil, status.Errorf(codes.Internal, "Elasticsearch search failed: %v", err)
	}
	defer res.Body.Close()

	if res.IsError() {
		var e map[string]interface{}
		if err := json.NewDecoder(res.Body).Decode(&e); err != nil {
			return nil, status.Errorf(codes.Internal, "Failed to decode error response: %v", err)
		} else {
			return nil, status.Errorf(codes.Internal, "Error in response: %v", e)
		}
	}

	// Обработка результата
	var r map[string]interface{}
	if err := json.NewDecoder(res.Body).Decode(&r); err != nil {
		return nil, status.Errorf(codes.Internal, "Failed to decode search response: %v", err)
	}

	// Пример обработки результата и возвращения ответа
	var documents []*go_grpc_server.DocumentInfo
	for _, hit := range r["hits"].(map[string]interface{})["hits"].([]interface{}) {
		id := hit.(map[string]interface{})["_id"].(string)
		text := decodeUTF8(hit.(map[string]interface{})["_source"].(map[string]interface{})["text"].(string))
		mainWord := decodeUTF8(hit.(map[string]interface{})["_source"].(map[string]interface{})["mainWord"].(string))
		document := &go_grpc_server.DocumentInfo{Id: &id, Text: text, MainWord: mainWord}
		documents = append(documents, document)
	}

	response := &go_grpc_server.FindExampleResponse{
		Documents: documents,
	}
	return response, nil
}

func decodeUTF8(text string) string {
	decodedText, err := strconv.Unquote(`"` + text + `"`)
	if err != nil {
		log.Printf("Failed to decode text: %v", err)
		return text
	}
	return decodedText
}

//func addDocument(esClient *elasticsearch.Client, indexName string) error {
//resIdx, errIdx :=
//esClient.Indices.Create(indexName)
//if errIdx != nil {
//	fmt.Printf("error creating index: %s", errIdx)
//}
//defer resIdx.Body.Close()
//if resIdx.IsError() {
//	var e map[string]interface{}
//	if err := json.NewDecoder(resIdx.Body).Decode(&e); err != nil {
//		return fmt.Errorf("error parsing the response body: %s", err)
//	}
//	return fmt.Errorf("error creating index: %s", e)
//}
//
//docJSON, err := json.Marshal(doc)
//if err != nil {
//	return err
//}
//
//res, err := esClient.Index(indexName, bytes.NewReader(docJSON), esClient.Index.WithContext(context.Background()))
//
//defer res.Body.Close()
//
//log.Printf("Document indexed with ID: %s", res.String())
//return nil
//}

func main() {
	err := godotenv.Load()
	if err != nil {
		log.Fatal("Error loading .env file")
	}

	esConfig := elasticsearch.Config{
		Addresses: []string{"http://localhost:9200"},
		Username:  "elastic",
		Password:  "elastic",
	}
	esClient, err := elasticsearch.NewClient(esConfig)
	if err != nil {
		log.Fatalf("Error creating Elasticsearch client: %s", err)
	}
	//addDocument(esClient, "test2")

	lis, err := net.Listen("tcp", ":1111")
	if err != nil {
		log.Fatalf("Failed to listen: %v", err)
	}
	defer lis.Close()

	server := grpc.NewServer()
	go_grpc_server.RegisterFindExamplesServiceServer(server, &findExampleServer{esClient: esClient})
	if err := server.Serve(lis); err != nil {
		log.Fatalf("Failed to serve: %v", err)
	}
}
