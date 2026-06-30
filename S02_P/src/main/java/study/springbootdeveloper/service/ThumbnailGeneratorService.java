package study.springbootdeveloper.service;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import study.springbootdeveloper.dto.GeneratorThumbnailRequest;
import study.springbootdeveloper.dto.GeneratorThumbnailResponse;
import study.springbootdeveloper.dto.UploadResponse;

import java.net.URI;
import java.util.Base64;

@Service
public class ThumbnailGeneratorService {

    private final ImageModel imageModel;
    private final PromptTemplate template;
    private final FileStorageService fileStorageService;
    private final RestClient restClient = RestClient.create();

    public ThumbnailGeneratorService(
            ImageModel imageModel,
            FileStorageService fileStorageService,
            @Value("classpath:prompts/thumbnail-generator.st") Resource promptResource
    ) {
        this.imageModel = imageModel;
        this.fileStorageService = fileStorageService;
        this.template = new PromptTemplate(promptResource);
    }

    public GeneratorThumbnailResponse generatorThumbnail(GeneratorThumbnailRequest request) {

        String prompt = template.create(request.toMap()).getContents();

        ImageResponse response = imageModel.call(new ImagePrompt(prompt));
        Image image = response.getResult().getOutput(); // 생성된 이미지 정보 가져오기

//        byte[] bytes = restClient.get() // 이미지 URL로부터 바이트 배열 가져오기
//                .uri(URI.create(image.getUrl()))
//                .retrieve()
//                .body(byte[].class);

        byte[] bytes = null;

        // 수정된 부분: Base64 데이터가 있는지 먼저 확인하고, 없으면 URL로 다운로드합니다.
        if (image.getB64Json() != null) {
            // 1. AI가 이미지를 텍스트 데이터(Base64)로 직접 준 경우 (RestClient 불필요!)
            bytes = Base64.getDecoder().decode(image.getB64Json());
        } else if (image.getUrl() != null) {
            // 2. AI가 이미지 URL 링크를 준 경우 (기존 방식)
            bytes = restClient.get()
                    .uri(URI.create(image.getUrl()))
                    .retrieve()
                    .body(byte[].class);
        } else {
            throw new RuntimeException("AI 응답에 이미지 데이터가 없습니다.");
        }

        UploadResponse saved = fileStorageService.store(bytes, "thumbnail.png");

        return new GeneratorThumbnailResponse(saved.imageUrl());
    }
}
