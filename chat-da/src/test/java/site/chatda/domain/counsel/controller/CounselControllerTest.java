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
import static com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.chatda.global.statuscode.ErrorCode.*;
import static site.chatda.global.statuscode.SuccessCode.CREATED;
import static site.chatda.global.statuscode.SuccessCode.OK;
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
    private static final String teacherUUID = "5a1e826e-2a44-4fea-98b2-bb96887b9737";
    private String studentToken;
    private String teacherToken;

    @PostConstruct
    public void init() {
        studentToken = jwtUtils.createToken(studentUUID);
        teacherToken = jwtUtils.createToken(teacherUUID);
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
                                        headerWithName("Authorization").description("학생 어세스 토큰")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")

                                        )
                                )
                                .requestSchema(Schema.schema("학생 상담 신청 Request"))
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
                        "학생 상담 신청 실패",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("학생 상담 신청 API")
                                .requestHeaders(
                                        headerWithName("Authorization").description("학생 어세스 토큰")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")

                                        )
                                )
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("상담 생성 성공")
    public void counsel_open_success() throws Exception {

        // given
        Long studentId = 2L;

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/counsels/students/{studentId}", studentId)
                        .header("Authorization", "Bearer " + teacherToken)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.header.message").value(CREATED.getMessage()))
                .andDo(document(
                        "상담 생성 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("상담 생성 API")
                                .requestHeaders(
                                        headerWithName("Authorization").description("선생 어세스 토큰")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")

                                        )
                                )
                                .requestSchema(Schema.schema("상담 생성 Request"))
                                .responseSchema(Schema.schema("상담 생성 Response"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("상담 생성 실패 - 이미 열려있는 상담")
    public void counsel_open_fail_already_exists() throws Exception {

        // given
        Long studentId = 3L;

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/counsels/students/{studentId}", studentId)
                        .header("Authorization", "Bearer " + teacherToken)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.header.message").value(COUNSEL_ALREADY_EXISTS.getMessage()))
                .andDo(document(
                        "상담 생성 실패 - 이미 열려있는 상담",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("상담 생성 API")
                                .requestHeaders(
                                        headerWithName("Authorization").description("선생 어세스 토큰")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")

                                        )
                                )
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("상담 생성 실패 - 다른 반 학생")
    public void counsel_open_fail_other_class_student() throws Exception {

        // given
        Long studentId = 10002L;

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/counsels/students/{studentId}", studentId)
                        .header("Authorization", "Bearer " + teacherToken)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.header.message").value(FORBIDDEN.getMessage()))
                .andDo(document(
                        "상담 생성 실패 - 다른 반 학생",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("상담 생성 API")
                                .requestHeaders(
                                        headerWithName("Authorization").description("선생 어세스 토큰")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")

                                        )
                                )
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("상담 생성 실패 - 존재하지 않는 학생 아이디")
    public void counsel_open_fail_wrong_student_id() throws Exception {

        // given
        Long studentId = 20002L;

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/counsels/students/{studentId}", studentId)
                        .header("Authorization", "Bearer " + teacherToken)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.message").value(NOT_FOUND.getMessage()))
                .andDo(document(
                        "상담 생성 실패 - 존재하지 않는 학생 아이디",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("상담 생성 API")
                                .requestHeaders(
                                        headerWithName("Authorization").description("선생 어세스 토큰")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")

                                        )
                                )
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("내 상담 내역 조회 성공")
    public void my_counsel_list_success() throws Exception {

        // given

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/counsels")
                        .header("Authorization", "Bearer " + studentToken)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "내 상담 내역 조회 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("내 상담 내역 조회 API")
                                .requestHeaders(
                                        headerWithName("Authorization").description("학생 어세스 토큰")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.counsels").type(ARRAY)
                                                        .description("상담 내역"),
                                                fieldWithPath("body.counsels[].counselId").type(NUMBER)
                                                        .description("상담 아이디"),
                                                fieldWithPath("body.counsels[].teacherName").type(STRING)
                                                        .description("담당 선생님"),
                                                fieldWithPath("body.counsels[].step").type(STRING)
                                                        .description("상담 단계"),
                                                fieldWithPath("body.counsels[].startDate").type(STRING)
                                                        .description("상담 시작 날짜"),
                                                fieldWithPath("body.counsels[].endDate").type(STRING)
                                                        .description("상담 종료 날짜")
                                        )
                                )
                                .requestSchema(Schema.schema("상내 상담 내역 조회 Request"))
                                .responseSchema(Schema.schema("내 상담 내역 조회 Response"))
                                .build()
                        ))
                );
    }
}
