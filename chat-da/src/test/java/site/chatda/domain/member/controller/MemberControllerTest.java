package site.chatda.domain.member.controller;

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
import static com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
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
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    private static final String studentUUID = "78954910-7440-4241-bd9d-ca3abc44d291";
    private static final String teacherUUID = "5a1e826e-2a44-4fea-98b2-bb96887b9737";
    private String studentToken;
    private String teacherToken;

    @PostConstruct
    public void init() {
        studentToken = jwtUtils.createToken(studentUUID);
        teacherToken = jwtUtils.createToken(teacherUUID);
    }

    @Test
    @DisplayName("학생 정보 조회 성공")
    public void member_details_student_success() throws Exception {

        // given

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/members")
                        .header("Authorization", "Bearer " + studentToken)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "학생 정보 조회 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Member API")
                                .summary("멤버 정보조회 API")
                                .requestHeaders(
                                        headerWithName("Authorization").description("어세스 토큰")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.schoolName").type(STRING)
                                                        .description("학교 이름"),
                                                fieldWithPath("body.level").type(NUMBER)
                                                        .description("학년"),
                                                fieldWithPath("body.classNumber").type(NUMBER)
                                                        .description("반"),
                                                fieldWithPath("body.name").type(STRING)
                                                        .description("학생 이름")
                                        )
                                )
                                .responseSchema(Schema.schema("학생 정보 조회 Response"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("선생 정보 조회 성공")
    public void member_details_teacher_success() throws Exception {

        // given

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/members")
                        .header("Authorization", "Bearer " + teacherToken)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "선생 정보 조회 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Member API")
                                .summary("멤버 정보조회 API")
                                .requestHeaders(
                                        headerWithName("Authorization").description("어세스 토큰")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.schoolName").type(STRING)
                                                        .description("학교 이름"),
                                                fieldWithPath("body.level").type(NUMBER)
                                                        .description("학년"),
                                                fieldWithPath("body.classNumber").type(NUMBER)
                                                        .description("반"),
                                                fieldWithPath("body.name").type(STRING)
                                                        .description("선생 이름")
                                        )
                                )
                                .responseSchema(Schema.schema("선생 정보 조회 Response"))
                                .build()
                        ))
                );
    }
}
