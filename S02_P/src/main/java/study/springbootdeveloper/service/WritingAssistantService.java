package study.springbootdeveloper.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import study.springbootdeveloper.dto.WritingSuggestionRequest;
import study.springbootdeveloper.dto.WritingSuggestionResponse;

@Slf4j
@Service
public class WritingAssistantService {

    private final ChatClient chatClient;
    // - RestTemplate나 WebClient처럼 LLM으로 HTTP 요청을 보내 응답을 받는 역할
    private final PromptTemplate template;

    // LLM의 json 응답을 WritingSuggestions DTO로 변환하기 위한 컨버터
    // - LLM은 기본적으로 일반 텍스트만 반환
    // - outputConverter.getFormat()을 호출하면, Spring AI가 내부적으로 WritingSuggestionResponse의 구조를 분석하고 JSON 스키마 요구사항 문자열을 만들어냄
    //      - 현재: {"suggestions" : ["string", "string", ...]}이여야해
    //      - 여기서 생성된 문자열이 아래 toMap에 의해서 format에 들어감
    /*
    제목: 오늘의 일기
    현재 작성된 내용: 샌드위치를 만들었다.
    질문: 이 다음에는 어떤 내용을 쓰는게 좋을까요?

    위 질문에 대해 3개의 구체적이고 실용적인 제안을 해주세요.

    Your response should be in JSON format.
    Do not include any explanations, only provide a RFC8259 compliant JSON response following this format without deviation.
    Do not include markdown code blocks in your response.
    Remove the ```json markdown from the output.
    Here is the JSON Schema instance your output must adhere to:
    ```{
      "$schema" : "https://json-schema.org/draft/2020-12/schema",
      "type" : "object",
      "properties" : {
        "suggestions" : {
          "type" : "array",
          "items" : {
            "type" : "string"
          }
        }
      },
      "required" : [ "suggestions" ],
      "additionalProperties" : false
    }```
     */
    private final BeanOutputConverter<WritingSuggestionResponse> outputConverter  = new BeanOutputConverter<>(WritingSuggestionResponse.class);

    public WritingAssistantService(
            ChatClient.Builder chatClientBuilder,
            @Value("classpath:prompts/writing-assistant.st") Resource promptResource
    ) {
        // - PromptTemplate과 .st 파일로 복잡한 프롬프트 문자열을 하드코딩하지 않고, 외부 파일로 분리하여 관리할 수 있음
        this.chatClient = chatClientBuilder.build();
        this.template = new PromptTemplate(promptResource);
    }

    public WritingSuggestionResponse getWritingAssist(WritingSuggestionRequest request) {

        // 프롬프트 템플릿에 파라미터를 넣어 최종 프롬프트 생성
        // - template.create(Map<String, Object> params)를 호출하면 맵체 담긴 키값과 .st 파일 내의 {...}변수와 매핑하여 하나의 완성된 텍스트 프롬프트를 생성해줌
        String prompt = template.create(request.toMap(outputConverter.getFormat())).getContents();

        log.info("prompt: {}", prompt);

        // LM에 프롬프트 전송 및 수신
        // - OpenAI Chat Completions API 규격에 맞게 JSON 요청 바디를 만들고, API 키를 헤더에 담아 전송
        // - 응답받은 JSON에서 메시지 내용만 쏙 뽑아오는 역할을 함
        String response = chatClient.prompt().user(prompt).call().content();
        log.info("response: {}", response);

        // 응답을 DTO로 변환
        // - JSON 텍스트를 반환함
        // - 근데 데이터 정합서 이슈: 올바른 응답 형식으로 오지 않아서 발생하는 경우가 있네
        return outputConverter.convert(response);
    }
}

// 추가 공부 사항
/*
> System Prompt와 User Prompt 분리
- System Message: LLM의 역할, 어조, 지켜야할 절대 규칙
- User Message: 사용자가 실제로 요청한 동적인 데이터를 담음
- .system() / .user()를 나누어 체이닝할 수 있음

> 예외처리와 안전망
- Rate Limiting(Http 429): 단기간 너무 많은 요청을 보내면 OpenAI에서 차단함
    - Spring Retry를 도입하여 지수 백오프, 시간을 늘려가며 재시도를 구현해야함

- Timeout(Http 504): LLM이 글을 쓰다가 시간이 너무 오래 걸릴 수 있음
    - 적절한 타임아웃 설정이 필요함

- Parsing Error
    - LLM이 간혹 {format} 지시를 무시하고 이상한 텍스트를 섞어서 보낼 수 있음
    - BeanOutputConverter가 파싱 에러를 냄
    - Fallback 로직 필요함

> 스트리밍(한 글자씩 - WebFlux)
> 파라미터 미세조정
> 토큰 사용량 및 비용 모니터링
    - .call().chatResponse()를 호출하여 메타데이터를 확인 및 DB나 로그시스템에 기록하여 헤비 유저를 관리하거나 서비스 비용을 추적 가능
*/