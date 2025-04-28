package site.chatda.domain.counsel.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import site.chatda.global.jwt.JwtUtils;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes.NULL;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.chatda.global.statuscode.ErrorCode.COUNSEL_ALREADY_EXISTS;
import static site.chatda.global.statuscode.SuccessCode.CREATED;
import static site.chatda.utils.ResponseFieldUtils.getCommonResponseFields;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class CounselControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    private static final String studentUUID = "78954910-7440-4241-bd9d-ca3abc44d291";
    private String studentToken;

    @PostConstruct
    public void init() {
        studentToken = jwtUtils.createToken(studentUUID);
    }

    @Test
    @DisplayName("학생 상담 신청 성공")
    public void student_counsel_application_success() throws Exception {

        // given

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/counsels/students")
                        .header("Authorization", "Bearer " + studentToken)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.header.message").value(CREATED.getMessage()))
                .andDo(document(
                        "학생 상담 신청 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("학생 상담 신청 API")
                                .requestHeaders(
                                        headerWithName("Authorization").description("어세스 토큰")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                        )
                                )
                                .responseSchema(Schema.schema("학생 상담 신청 Response"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("학생 상담 신청 실패 - 이미 열려있는 상담")
    public void student_counsel_application_fail_counsel_already_exists() throws Exception {

        // given

        // when
        mockMvc.perform(
                post("/api/counsels/students")
                        .header("Authorization", "Bearer " + studentToken)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        ResultActions actions = mockMvc.perform(
                post("/api/counsels/students")
                        .header("Authorization", "Bearer " + studentToken)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.header.message").value(COUNSEL_ALREADY_EXISTS.getMessage()))
                .andDo(document(
                        "학생 상담 신청 실패 - 이미 열려있는 상담",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("학생 상담 신청 API")
                                .requestHeaders(
                                        headerWithName("Authorization").description("어세스 토큰")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                        )
                                )
                                .responseSchema(Schema.schema("학생 상담 신청 Response"))
                                .build()
                        ))
                );
    }
}
