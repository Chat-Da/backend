package site.chatda.global.jwt.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
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

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes.NULL;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.chatda.global.statuscode.SuccessCode.OK;
import static site.chatda.utils.ResponseFieldUtils.getCommonResponseFields;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("학생 로그인 성공")
    public void student_login_success() throws Exception {

        // given

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/auth/login/students")
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "학생 로그인 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Auth API")
                                .summary("학생 로그인 API")
                                .responseHeaders(
                                        headerWithName("Authorization").description("학생 어세스 토큰")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .responseSchema(Schema.schema("학생 로그인 Response"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("선생 로그인 성공")
    public void teacher_login_success() throws Exception {

        // given

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/auth/login/teachers")
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "선생 로그인 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Auth API")
                                .summary("선생 로그인 API")
                                .responseHeaders(
                                        headerWithName("Authorization").description("선생 어세스 토큰")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .responseSchema(Schema.schema("선생 로그인 Response"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("어드민 로그인 성공")
    public void admin_login_success() throws Exception {

        // given

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/auth/login/admin")
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "어드민 로그인 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Auth API")
                                .summary("어드민 로그인 API")
                                .responseHeaders(
                                        headerWithName("Authorization").description("어드민 어세스 토큰")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .responseSchema(Schema.schema("어드민 로그인 Response"))
                                .build()
                        ))
                );
    }
}
