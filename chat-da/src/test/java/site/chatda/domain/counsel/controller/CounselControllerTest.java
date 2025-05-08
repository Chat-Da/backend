package site.chatda.domain.counsel.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import jakarta.annotation.PostConstruct;
import org.json.JSONArray;
import org.json.JSONObject;
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

import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
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
    private static final String studentUUID2 = "54449b31-c04d-4c15-9136-c7e2a618564e";
    private static final String teacherUUID = "5a1e826e-2a44-4fea-98b2-bb96887b9737";
    private static final String adminUUID = "eca58d22-f409-465b-a179-b5d527b4f687";

    private String studentToken;
    private String studentToken2;
    private String teacherToken;
    private String adminToken;

    @PostConstruct
    public void init() {
        studentToken = jwtUtils.createToken(studentUUID);
        studentToken2 = jwtUtils.createToken(studentUUID2);
        teacherToken = jwtUtils.createToken(teacherUUID);
        adminToken = jwtUtils.createToken(adminUUID);
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
                                .pathParameters(
                                        List.of(
                                                parameterWithName("studentId")
                                                        .description("학생 아이디")
                                        )
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("교사 어세스 토큰")
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
                                .pathParameters(
                                        List.of(
                                                parameterWithName("studentId")
                                                        .description("학생 아이디")
                                        )
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("교사 어세스 토큰")
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
                                .pathParameters(
                                        List.of(
                                                parameterWithName("studentId")
                                                        .description("학생 아이디")
                                        )
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("교사 어세스 토큰")
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
                                .pathParameters(
                                        List.of(
                                                parameterWithName("studentId")
                                                        .description("학생 아이디")
                                        )
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("교사 어세스 토큰")
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
                                                        .description("담당 교사"),
                                                fieldWithPath("body.counsels[].step").type(STRING)
                                                        .description("상담 단계"),
                                                fieldWithPath("body.counsels[].startDate").type(STRING)
                                                        .description("상담 시작 날짜"),
                                                fieldWithPath("body.counsels[].endDate").type(STRING)
                                                        .description("상담 종료 날짜")
                                        )
                                )
                                .requestSchema(Schema.schema("내 상담 내역 조회 Request"))
                                .responseSchema(Schema.schema("내 상담 내역 조회 Response"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("담당 학생 상담 내역 조회 성공")
    public void student_counsel_list_success() throws Exception {

        // given
        Long studentId = 2L;

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/counsels/students/{studentId}", studentId)
                        .header("Authorization", "Bearer " + teacherToken)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "담당 학생 상담 내역 조회 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("담당 학생 상담 내역 조회 API")
                                .pathParameters(
                                        List.of(
                                                parameterWithName("studentId")
                                                        .description("학생 아이디")
                                        )
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("교사 어세스 토큰")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.counsels").type(ARRAY)
                                                        .description("상담 내역"),
                                                fieldWithPath("body.counsels[].counselId").type(NUMBER)
                                                        .description("상담 아이디"),
                                                fieldWithPath("body.counsels[].teacherName").type(STRING)
                                                        .description("담당 교사"),
                                                fieldWithPath("body.counsels[].step").type(STRING)
                                                        .description("상담 단계"),
                                                fieldWithPath("body.counsels[].startDate").type(STRING)
                                                        .description("상담 시작 날짜"),
                                                fieldWithPath("body.counsels[].endDate").type(STRING)
                                                        .description("상담 종료 날짜")
                                        )
                                )
                                .requestSchema(Schema.schema("담당 학생 상담 내역 조회 Request"))
                                .responseSchema(Schema.schema("담당 학생 상담 내역 조회 Response"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("담당 학생 상담 내역 조회 실패 - 다른 반 학생")
    public void student_counsel_list_fail_other_class() throws Exception {

        // given
        Long studentId = 10002L;

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/counsels/students/{studentId}", studentId)
                        .header("Authorization", "Bearer " + teacherToken)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.header.message").value(FORBIDDEN.getMessage()))
                .andDo(document(
                        "담당 학생 상담 내역 조회 실패 - 다른 반 학생",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("담당 학생 상담 내역 조회 API")
                                .pathParameters(
                                        List.of(
                                                parameterWithName("studentId")
                                                        .description("학생 아이디")
                                        )
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("교사 어세스 토큰")
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
    @DisplayName("담당 학생 상담 내역 조회 실패 - 존재하지 않는 학생 아이디")
    public void student_counsel_list_fail_wrong_student_id() throws Exception {

        // given
        Long studentId = 20002L;

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/counsels/students/{studentId}", studentId)
                        .header("Authorization", "Bearer " + teacherToken)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.message").value(NOT_FOUND.getMessage()))
                .andDo(document(
                        "담당 학생 상담 내역 조회 실패 - 존재하지 않는 학생 아이디",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("담당 학생 상담 내역 조회 API")
                                .pathParameters(
                                        List.of(
                                                parameterWithName("studentId")
                                                        .description("학생 아이디")
                                        )
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("교사 어세스 토큰")
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
    @DisplayName("상담 단계 변경 성공")
    public void counsel_step_change_success() throws Exception {

        // given
        Long counselId = 5L;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("step", "COMPLETED");

        String content = jsonObject.toString();

        // when
        ResultActions actions = mockMvc.perform(
                patch("/api/counsels/{counselId}", counselId)
                        .header("Authorization", "Bearer " + teacherToken)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "상담 단계 변경 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("상담 단계 변경 API")
                                .pathParameters(
                                        List.of(
                                                parameterWithName("counselId")
                                                        .description("상담 아이디")
                                        )
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("어세스 토큰")
                                )
                                .requestFields(
                                    List.of(
                                            fieldWithPath("step").type(STRING)
                                                    .description("변경할 상담 단계 (상담 시작 : IN_PROGRESS, 상담 완료 : COMPLETED)")
                                    )
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("상담 단계 변경 Request"))
                                .responseSchema(Schema.schema("상담 단계 변경 Response"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("상담 단계 변경 실패 - 잘못된 단계 입력")
    public void counsel_step_change_fail_wrong_step() throws Exception {

        // given
        Long counselId = 5L;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("step", "IN_PROGRESS");

        String content = jsonObject.toString();

        // when
        ResultActions actions = mockMvc.perform(
                patch("/api/counsels/{counselId}", counselId)
                        .header("Authorization", "Bearer " + teacherToken)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.header.message").value(BAD_REQUEST.getMessage()))
                .andDo(document(
                        "상담 단계 변경 실패 - 잘못된 단계 입력",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("상담 단계 변경 API")
                                .pathParameters(
                                        List.of(
                                                parameterWithName("counselId")
                                                        .description("상담 아이디")
                                        )
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("어세스 토큰")
                                )
                                .requestFields(
                                        List.of(
                                                fieldWithPath("step").type(STRING)
                                                        .description("변경할 상담 단계")
                                        )
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
    @DisplayName("상담 단계 변경 실패 - 권한 없음")
    public void counsel_step_change_fail_not_my_counsel() throws Exception {

        // given
        Long counselId = 5L;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("step", "COMPLETED");

        String content = jsonObject.toString();

        // when
        ResultActions actions = mockMvc.perform(
                patch("/api/counsels/{counselId}", counselId)
                        .header("Authorization", "Bearer " + studentToken)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.header.message").value(FORBIDDEN.getMessage()))
                .andDo(document(
                        "상담 단계 변경 실패 - 다른 사람의 상담",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("상담 단계 변경 API")
                                .pathParameters(
                                        List.of(
                                                parameterWithName("counselId")
                                                        .description("상담 아이디")
                                        )
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("어세스 토큰")
                                )
                                .requestFields(
                                        List.of(
                                                fieldWithPath("step").type(STRING)
                                                        .description("변경할 상담 단계")
                                        )
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
    @DisplayName("상담 단계 변경 실패 - 존재하지 않는 상담")
    public void counsel_step_change_fail_wrong_counsel_id() throws Exception {

        // given
        Long counselId = 20005L;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("step", "COMPLETED");

        String content = jsonObject.toString();

        // when
        ResultActions actions = mockMvc.perform(
                patch("/api/counsels/{counselId}", counselId)
                        .header("Authorization", "Bearer " + teacherToken)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.message").value(NOT_FOUND.getMessage()))
                .andDo(document(
                        "상담 단계 변경 실패 - 존재하지 않는 상담",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("상담 단계 변경 API")
                                .pathParameters(
                                        List.of(
                                                parameterWithName("counselId")
                                                        .description("상담 아이디")
                                        )
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("어세스 토큰")
                                )
                                .requestFields(
                                        List.of(
                                                fieldWithPath("step").type(STRING)
                                                        .description("변경할 상담 단계")
                                        )
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
    @DisplayName("보고서 생성 성공")
    public void create_report_success() throws Exception {

        // given
        Long counselId = 6L;
        String content = getReportContent();

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/counsels/{counselId}/reports", counselId)
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.header.message").value(CREATED.getMessage()))
                .andDo(document(
                        "보고서 생성 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("보고서 생성 API")
                                .pathParameters(
                                        List.of(
                                                parameterWithName("counselId")
                                                        .description("상담 아이디")
                                        )
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("어드민 어세스 토큰")
                                )
                                .requestFields(
                                        List.of(
                                                fieldWithPath("personality").type(STRING)
                                                        .description("성향 요약 (500자 이내)"),
                                                fieldWithPath("selfAwareness").type(STRING)
                                                        .description("자기 이해도 (LOW, MIDDLE, HIGH 중 하나)"),
                                                fieldWithPath("selfAwarenessDescription").type(STRING)
                                                        .description("자기 이해도 설명 (500자 이내)"),
                                                fieldWithPath("strengthSummary").type(STRING)
                                                        .description("강점 요약 (500자 이내)"),
                                                fieldWithPath("weaknessSummary").type(STRING)
                                                        .description("약점 요약 (500자 이내)"),
                                                fieldWithPath("strengths").type(ARRAY)
                                                        .description("강점 리스트"),
                                                fieldWithPath("weaknesses").type(ARRAY)
                                                        .description("약점 리스트"),
                                                fieldWithPath("interests").type(ARRAY)
                                                        .description("흥미 리스트"),
                                                fieldWithPath("growthSuggestions").type(ARRAY)
                                                        .description("성장 제안"),
                                                fieldWithPath("jobSuggestions").type(ARRAY)
                                                        .description("직업 추천 (직업 아이디 리스트)"),
                                                fieldWithPath("jobSuggestionReasons").type(ARRAY)
                                                        .description("직업 추천 이유"),
                                                fieldWithPath("jobGrowthSuggestions").type(ARRAY)
                                                        .description("직업별 성장 제안")
                                        )
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("보고서 생성 Request"))
                                .responseSchema(Schema.schema("보고서 생성 Response"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("보고서 생성 실패 - 잘못된 상담 아이디")
    public void create_report_fail_wrong_counsel_id() throws Exception {

        // given
        Long counselId = 20001L;
        String content = getReportContent();

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/counsels/{counselId}/reports", counselId)
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.message").value(NOT_FOUND.getMessage()))
                .andDo(document(
                        "보고서 생성 실패 - 잘못된 상담 아이디",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("보고서 생성 API")
                                .pathParameters(
                                        List.of(
                                                parameterWithName("counselId")
                                                        .description("상담 아이디")
                                        )
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("어드민 어세스 토큰")
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
    @DisplayName("보고서 생성 실패 - 직업 추천 / 이유 / 성장 제안 개수 다름")
    public void create_report_fail_wrong_job_suggestions() throws Exception {

        // given
        Long counselId = 6L;
        String content = getWrongReportContent();

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/counsels/{counselId}/reports", counselId)
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.header.message").value(BAD_REQUEST.getMessage()))
                .andDo(document(
                        "보고서 생성 실패 - 직업 추천, 이유, 성장 제안 개수 다름",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("보고서 생성 API")
                                .pathParameters(
                                        List.of(
                                                parameterWithName("counselId")
                                                        .description("상담 아이디")
                                        )
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("어드민 어세스 토큰")
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
    @DisplayName("담당 교사 한마디 추가 성공")
    public void save_teacher_comment_success() throws Exception {

        // given
        Long counselId = 5L;

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("content", "담당 교사 한마디");

        String content = jsonObject.toString();

        // when
        ResultActions actions = mockMvc.perform(
                put("/api/counsels/{counselId}/comments", counselId)
                        .header("Authorization", "Bearer " + teacherToken)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.header.message").value(CREATED.getMessage()))
                .andDo(document(
                        "담당 교사 한마디 추가 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("담당 교사 한마디 추가 API")
                                .pathParameters(
                                        List.of(
                                                parameterWithName("counselId")
                                                        .description("상담 아이디")
                                        )
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("교사 어세스 토큰")
                                )
                                .requestFields(
                                        List.of(
                                                fieldWithPath("content").type(STRING)
                                                        .description("담당 교사 한마디 (500자 이내)")
                                        )
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("담당 교사 한마디 추가 Request"))
                                .responseSchema(Schema.schema("담당 교사 한마디 추가 Response"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("담당 교사 한마디 추가 실패 - 없는 보고서")
    public void save_teacher_comment_fail_wrong_report() throws Exception {

        // given
        Long counselId = 20005L;

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("content", "담당 교사 한마디");

        String content = jsonObject.toString();

        // when
        ResultActions actions = mockMvc.perform(
                put("/api/counsels/{counselId}/comments", counselId)
                        .header("Authorization", "Bearer " + teacherToken)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.message").value(NOT_FOUND.getMessage()))
                .andDo(document(
                        "담당 교사 한마디 추가 실패 - 없는 보고서",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("담당 교사 한마디 추가 API")
                                .pathParameters(
                                        List.of(
                                                parameterWithName("counselId")
                                                        .description("상담 아이디")
                                        )
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("교사 어세스 토큰")
                                )
                                .requestFields(
                                        List.of(
                                                fieldWithPath("content").type(STRING)
                                                        .description("담당 교사 한마디 (500자 이내)")
                                        )
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
    @DisplayName("담당 교사 한마디 추가 실패 - 결과 대기중이 아님")
    public void save_teacher_comment_fail_is_not_result_waiting() throws Exception {

        // given
        Long counselId = 3L;

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("content", "담당 교사 한마디");

        String content = jsonObject.toString();

        // when
        ResultActions actions = mockMvc.perform(
                put("/api/counsels/{counselId}/comments", counselId)
                        .header("Authorization", "Bearer " + teacherToken)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.header.message").value(BAD_REQUEST.getMessage()))
                .andDo(document(
                        "담당 교사 한마디 추가 실패 - 결과 대기중이 아님",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("담당 교사 한마디 추가 API")
                                .pathParameters(
                                        List.of(
                                                parameterWithName("counselId")
                                                        .description("상담 아이디")
                                        )
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("교사 어세스 토큰")
                                )
                                .requestFields(
                                        List.of(
                                                fieldWithPath("content").type(STRING)
                                                        .description("담당 교사 한마디 (500자 이내)")
                                        )
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
    @DisplayName("담당 교사 한마디 추가 실패 - 담당 학생 보고서가 아님")
    public void save_teacher_comment_fail_is_not_my_student() throws Exception {

        // given
        Long counselId = 1L;

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("content", "담당 교사 한마디");

        String content = jsonObject.toString();

        // when
        ResultActions actions = mockMvc.perform(
                put("/api/counsels/{counselId}/comments", counselId)
                        .header("Authorization", "Bearer " + teacherToken)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.header.message").value(FORBIDDEN.getMessage()))
                .andDo(document(
                        "담당 교사 한마디 추가 실패 - 담당 학생 보고서가 아님",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("담당 교사 한마디 추가 API")
                                .pathParameters(
                                        List.of(
                                                parameterWithName("counselId")
                                                        .description("상담 아이디")
                                        )
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("교사 어세스 토큰")
                                )
                                .requestFields(
                                        List.of(
                                                fieldWithPath("content").type(STRING)
                                                        .description("담당 교사 한마디 (500자 이내)")
                                        )
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
    @DisplayName("담당 교사 직업 추천 추가 성공")
    public void save_teacher_job_suggestion_success() throws Exception {

        // given
        Long counselId = 5L;

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("content", "담당 교사 직업 추천");

        String content = jsonObject.toString();

        // when
        ResultActions actions = mockMvc.perform(
                put("/api/counsels/{counselId}/job_suggestions", counselId)
                        .header("Authorization", "Bearer " + teacherToken)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.header.message").value(CREATED.getMessage()))
                .andDo(document(
                        "담당 교사 직업 추천 추가 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("담당 교사 직업 추천 추가 API")
                                .pathParameters(
                                        List.of(
                                                parameterWithName("counselId")
                                                        .description("상담 아이디")
                                        )
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("교사 어세스 토큰")
                                )
                                .requestFields(
                                        List.of(
                                                fieldWithPath("content").type(STRING)
                                                        .description("담당 교사 직업 추천 (500자 이내)")
                                        )
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("담당 교사 직업 추천 추가 Request"))
                                .responseSchema(Schema.schema("담당 교사 직업 추천 추가 Response"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("담당 교사 직업 추천 추가 실패 - 없는 보고서")
    public void save_teacher_job_suggestion_fail_wrong_report() throws Exception {

        // given
        Long counselId = 20005L;

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("content", "담당 교사 직업 추천");

        String content = jsonObject.toString();

        // when
        ResultActions actions = mockMvc.perform(
                put("/api/counsels/{counselId}/job_suggestions", counselId)
                        .header("Authorization", "Bearer " + teacherToken)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.message").value(NOT_FOUND.getMessage()))
                .andDo(document(
                        "담당 교사 직업 추천 추가 실패 - 없는 보고서",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("담당 교사 직업 추천 추가 API")
                                .pathParameters(
                                        List.of(
                                                parameterWithName("counselId")
                                                        .description("상담 아이디")
                                        )
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("교사 어세스 토큰")
                                )
                                .requestFields(
                                        List.of(
                                                fieldWithPath("content").type(STRING)
                                                        .description("담당 교사 직업 추천 (500자 이내)")
                                        )
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
    @DisplayName("담당 교사 직업 추천 추가 실패 - 결과 대기중이 아님")
    public void save_teacher_job_suggestion_fail_is_not_result_waiting() throws Exception {

        // given
        Long counselId = 3L;

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("content", "담당 교사 직업추천");

        String content = jsonObject.toString();

        // when
        ResultActions actions = mockMvc.perform(
                put("/api/counsels/{counselId}/job_suggestions", counselId)
                        .header("Authorization", "Bearer " + teacherToken)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.header.message").value(BAD_REQUEST.getMessage()))
                .andDo(document(
                        "담당 교사 직업 추천 추가 실패 - 결과 대기중이 아님",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("담당 교사 직업 추천 추가 API")
                                .pathParameters(
                                        List.of(
                                                parameterWithName("counselId")
                                                        .description("상담 아이디")
                                        )
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("교사 어세스 토큰")
                                )
                                .requestFields(
                                        List.of(
                                                fieldWithPath("content").type(STRING)
                                                        .description("담당 교사 한마디 (500자 이내)")
                                        )
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
    @DisplayName("담당 교사 직업 추천 추가 실패 - 담당 학생 보고서가 아님")
    public void save_teacher_job_suggestion_fail_is_not_my_student() throws Exception {

        // given
        Long counselId = 1L;

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("content", "담당 교사 직업 추천");

        String content = jsonObject.toString();

        // when
        ResultActions actions = mockMvc.perform(
                put("/api/counsels/{counselId}/job_suggestions", counselId)
                        .header("Authorization", "Bearer " + teacherToken)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.header.message").value(FORBIDDEN.getMessage()))
                .andDo(document(
                        "담당 교사 직업 추천 추가 실패 - 담당 학생 보고서가 아님",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("담당 교사 직업 추천 추가 API")
                                .pathParameters(
                                        List.of(
                                                parameterWithName("counselId")
                                                        .description("상담 아이디")
                                        )
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("교사 어세스 토큰")
                                )
                                .requestFields(
                                        List.of(
                                                fieldWithPath("content").type(STRING)
                                                        .description("담당 교사 직업 추천 (500자 이내)")
                                        )
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
    @DisplayName("담당 교사 성장 지도 추가 성공")
    public void save_teacher_guidance_success() throws Exception {

        // given
        Long counselId = 5L;
        Integer seq = 1;

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("content", "담당 교사 성장 지도");

        String content = jsonObject.toString();

        // when
        ResultActions actions = mockMvc.perform(
                put("/api/counsels/{counselId}/guidance/{seq}", counselId, seq)
                        .header("Authorization", "Bearer " + teacherToken)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.header.message").value(CREATED.getMessage()))
                .andDo(document(
                        "담당 교사 성장 지도 추가 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("담당 교사 성장 지도 추가 API")
                                .requestHeaders(
                                        headerWithName("Authorization").description("교사 어세스 토큰")
                                )
                                .pathParameters(
                                        List.of(
                                                parameterWithName("counselId")
                                                        .description("상담 아이디"),
                                                parameterWithName("seq")
                                                        .description("번호")
                                        )
                                )
                                .requestFields(
                                        List.of(
                                                fieldWithPath("content").type(STRING)
                                                        .description("담당 교사 성장 지도 (200자 이내)")
                                        )
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("담당 교사 성장 지도 추가 Request"))
                                .responseSchema(Schema.schema("담당 교사 성장 지도 추가 Response"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("담당 교사 성장 지도 추가 실패 - 없는 보고서")
    public void save_teacher_guidance_fail_wrong_report() throws Exception {

        // given
        Long counselId = 20005L;

        Integer seq = 1;

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("content", "담당 교사 성장 지도");

        String content = jsonObject.toString();

        // when
        ResultActions actions = mockMvc.perform(
                put("/api/counsels/{counselId}/guidance/{seq}", counselId, seq)
                        .header("Authorization", "Bearer " + teacherToken)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.message").value(NOT_FOUND.getMessage()))
                .andDo(document(
                        "담당 교사 성장 지도 추가 실패 - 없는 보고서",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("담당 교사 성장 지도 추가 API")
                                .requestHeaders(
                                        headerWithName("Authorization").description("교사 어세스 토큰")
                                )
                                .requestFields(
                                        List.of(
                                                fieldWithPath("content").type(STRING)
                                                        .description("담당 교사 성장 지도 (500자 이내)")
                                        )
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
    @DisplayName("담당 교사 직업 추천 추가 실패 - 잘못된 번호")
    public void save_teacher_guidance_fail_wrong_seq() throws Exception {

        // given
        Long counselId = 5L;

        Integer seq = 4;

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("content", "담당 교사 성장 지도");

        String content = jsonObject.toString();

        // when
        ResultActions actions = mockMvc.perform(
                put("/api/counsels/{counselId}/guidance/{seq}", counselId, seq)
                        .header("Authorization", "Bearer " + teacherToken)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.header.message").value(BAD_REQUEST.getMessage()))
                .andDo(document(
                        "담당 교사 직업 추천 추가 실패 - 잘못된 번호",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("담당 교사 성장 지도 추가 API")
                                .requestHeaders(
                                        headerWithName("Authorization").description("교사 어세스 토큰")
                                )
                                .requestFields(
                                        List.of(
                                                fieldWithPath("content").type(STRING)
                                                        .description("담당 교사 성장 지도 (200자 이내)")
                                        )
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
    @DisplayName("담당 교사 직업 추천 추가 실패 - 결과 대기중이 아님")
    public void save_teacher_guidance_fail_is_not_result_waiting() throws Exception {

        // given
        Long counselId = 3L;

        Integer seq = 1;

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("content", "담당 교사 성장 지도");

        String content = jsonObject.toString();

        // when
        ResultActions actions = mockMvc.perform(
                put("/api/counsels/{counselId}/guidance/{seq}", counselId, seq)
                        .header("Authorization", "Bearer " + teacherToken)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.header.message").value(BAD_REQUEST.getMessage()))
                .andDo(document(
                        "담당 교사 성장 지도 추가 실패 - 결과 대기중이 아님",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("담당 교사 성장 지도 추가 API")
                                .requestHeaders(
                                        headerWithName("Authorization").description("교사 어세스 토큰")
                                )
                                .requestFields(
                                        List.of(
                                                fieldWithPath("content").type(STRING)
                                                        .description("담당 교사 성장 지도 (200자 이내)")
                                        )
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
    @DisplayName("담당 교사 직업 추천 추가 실패 - 담당 학생 보고서가 아님")
    public void save_guidance_fail_is_not_my_student() throws Exception {

        // given
        Long counselId = 1L;

        Integer seq = 1;

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("content", "담당 교사 성장 지도");

        String content = jsonObject.toString();

        // when
        ResultActions actions = mockMvc.perform(
                put("/api/counsels/{counselId}/guidance/{seq}", counselId, seq)
                        .header("Authorization", "Bearer " + teacherToken)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.header.message").value(FORBIDDEN.getMessage()))
                .andDo(document(
                        "담당 교사 성장 지도 추가 실패 - 담당 학생 보고서가 아님",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("담당 교사 성장 지도 추가 API")
                                .requestHeaders(
                                        headerWithName("Authorization").description("교사 어세스 토큰")
                                )
                                .requestFields(
                                        List.of(
                                                fieldWithPath("content").type(STRING)
                                                        .description("담당 교사 성장 지도 (200자 이내)")
                                        )
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
    @DisplayName("보고서 조회 성공 - 학생(최종 x)")
    public void find_report_details_success_student_not_final() throws Exception {

        // given
        Long counselId = 6L;

        // when
        createReport();

        ResultActions actions = mockMvc.perform(
                get("/api/counsels/{counselId}/reports", counselId)
                        .header("Authorization", "Bearer " + studentToken2)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "보고서 조회 성공 - 학생(최종x)",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("보고서 조회 API")
                                .pathParameters(
                                        List.of(
                                                parameterWithName("counselId")
                                                        .description("상담 아이디")
                                        )
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("학생 어세스 토큰")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.studentInfo").type(OBJECT)
                                                        .description("학생 정보"),
                                                fieldWithPath("body.studentInfo.name").type(STRING)
                                                        .description("학생 이름"),
                                                fieldWithPath("body.studentInfo.schoolName").type(STRING)
                                                        .description("학교 이름"),
                                                fieldWithPath("body.studentInfo.grade").type(NUMBER)
                                                        .description("학년"),
                                                fieldWithPath("body.teacherInfo").type(OBJECT)
                                                        .description("담당교사 정보"),
                                                fieldWithPath("body.teacherInfo.name").type(STRING)
                                                        .description("교사 이름"),
                                                fieldWithPath("body.teacherInfo.schoolName").type(STRING)
                                                        .description("학교 이름"),
                                                fieldWithPath("body.counselId").type(NUMBER)
                                                        .description("상담 아이디"),
                                                fieldWithPath("body.counselDate").type(STRING)
                                                        .description("상담 시작일"),
                                                fieldWithPath("body.counselStep").type(STRING)
                                                        .description("상담 단계"),
                                                fieldWithPath("body.personality").type(STRING)
                                                        .description("성향 요약"),
                                                fieldWithPath("body.selfAwareness").type(STRING)
                                                        .description("자기 이해도"),
                                                fieldWithPath("body.selfAwarenessDescription").type(STRING)
                                                        .description("자기 이해도 설명"),
                                                fieldWithPath("body.strengthSummary").type(STRING)
                                                        .description("강점 요약"),
                                                fieldWithPath("body.weaknessSummary").type(STRING)
                                                        .description("약점 요약"),
                                                fieldWithPath("body.strengths").type(ARRAY)
                                                        .description("강점 리스트"),
                                                fieldWithPath("body.weaknesses").type(ARRAY)
                                                        .description("약점 리스트"),
                                                fieldWithPath("body.interests").type(ARRAY)
                                                        .description("흥미 리스트"),
                                                fieldWithPath("body.jobRecommendations").type(ARRAY)
                                                        .description("추천 직업 리스트"),
                                                fieldWithPath("body.jobRecommendations[].id").type(NUMBER)
                                                        .description("직업 아이디"),
                                                fieldWithPath("body.jobRecommendations[].name").type(STRING)
                                                        .description("직업 이름"),
                                                fieldWithPath("body.jobRecommendations[].recommendReason").type(STRING)
                                                        .description("직업 추천 이유"),
                                                fieldWithPath("body.jobRecommendations[].growthSuggestion").type(STRING)
                                                        .description("직업 성장 제안"),
                                                fieldWithPath("body.jobRecommendations[].requiredSkills").type(ARRAY)
                                                        .description("필요 역량"),
                                                fieldWithPath("body.growthSuggestions").type(ARRAY)
                                                        .description("성장 제안 리스트"),
                                                fieldWithPath("body.teacherFeedback").type(NULL)
                                                        .description("교사 피드백(내용 없음)")
                                        )
                                )
                                .requestSchema(Schema.schema("보고서 조회 (학생 , 최종 x) Request"))
                                .responseSchema(Schema.schema("보고서 조회 (학생 , 최종 x) Response"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("보고서 조회 성공 - 교사")
    public void find_report_details_success_teacher() throws Exception {

        // given
        Long counselId = 6L;

        // when
        createReport();

        ResultActions actions = mockMvc.perform(
                get("/api/counsels/{counselId}/reports", counselId)
                        .header("Authorization", "Bearer " + teacherToken)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "보고서 조회 성공 - 교사",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("보고서 조회 API")
                                .pathParameters(
                                        List.of(
                                                parameterWithName("counselId")
                                                        .description("상담 아이디")
                                        )
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("교사 어세스 토큰")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.studentInfo").type(OBJECT)
                                                        .description("학생 정보"),
                                                fieldWithPath("body.studentInfo.name").type(STRING)
                                                        .description("학생 이름"),
                                                fieldWithPath("body.studentInfo.schoolName").type(STRING)
                                                        .description("학교 이름"),
                                                fieldWithPath("body.studentInfo.grade").type(NUMBER)
                                                        .description("학년"),
                                                fieldWithPath("body.teacherInfo").type(OBJECT)
                                                        .description("담당교사 정보"),
                                                fieldWithPath("body.teacherInfo.name").type(STRING)
                                                        .description("교사 이름"),
                                                fieldWithPath("body.teacherInfo.schoolName").type(STRING)
                                                        .description("학교 이름"),
                                                fieldWithPath("body.counselId").type(NUMBER)
                                                        .description("상담 아이디"),
                                                fieldWithPath("body.counselDate").type(STRING)
                                                        .description("상담 시작일"),
                                                fieldWithPath("body.counselStep").type(STRING)
                                                        .description("상담 단계"),
                                                fieldWithPath("body.personality").type(STRING)
                                                        .description("성향 요약"),
                                                fieldWithPath("body.selfAwareness").type(STRING)
                                                        .description("자기 이해도"),
                                                fieldWithPath("body.selfAwarenessDescription").type(STRING)
                                                        .description("자기 이해도 설명"),
                                                fieldWithPath("body.strengthSummary").type(STRING)
                                                        .description("강점 요약"),
                                                fieldWithPath("body.weaknessSummary").type(STRING)
                                                        .description("약점 요약"),
                                                fieldWithPath("body.strengths").type(ARRAY)
                                                        .description("강점 리스트"),
                                                fieldWithPath("body.weaknesses").type(ARRAY)
                                                        .description("약점 리스트"),
                                                fieldWithPath("body.interests").type(ARRAY)
                                                        .description("흥미 리스트"),
                                                fieldWithPath("body.jobRecommendations").type(ARRAY)
                                                        .description("추천 직업 리스트"),
                                                fieldWithPath("body.jobRecommendations[].id").type(NUMBER)
                                                        .description("직업 아이디"),
                                                fieldWithPath("body.jobRecommendations[].name").type(STRING)
                                                        .description("직업 이름"),
                                                fieldWithPath("body.jobRecommendations[].recommendReason").type(STRING)
                                                        .description("직업 추천 이유"),
                                                fieldWithPath("body.jobRecommendations[].growthSuggestion").type(STRING)
                                                        .description("직업 성장 제안"),
                                                fieldWithPath("body.jobRecommendations[].requiredSkills").type(ARRAY)
                                                        .description("필요 역량"),
                                                fieldWithPath("body.growthSuggestions").type(ARRAY)
                                                        .description("성장 제안 리스트"),
                                                fieldWithPath("body.teacherFeedback").type(OBJECT)
                                                        .description("교사 피드백"),
                                                fieldWithPath("body.teacherFeedback.comment").type(STRING)
                                                        .description("담당교사 한마디"),
                                                fieldWithPath("body.teacherFeedback.jobSuggestion").type(STRING)
                                                        .description("담당교사 직업 추천"),
                                                fieldWithPath("body.teacherFeedback.guidance").type(ARRAY)
                                                        .description("담당교사 성장 지도")
                                        )
                                )
                                .requestSchema(Schema.schema("보고서 조회 (교사) Request"))
                                .responseSchema(Schema.schema("보고서 조회 (교사) Response"))
                                .build()
                        ))
                );
    }

    @Test
    @DisplayName("보고서 조회 성공 - 학생(최종)")
    public void find_report_details_success_student_final() throws Exception {

        // given
        Long counselId = 1L;

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/counsels/{counselId}/reports", counselId)
                        .header("Authorization", "Bearer " + studentToken)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "보고서 조회 성공 - 학생(최종)",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Counsel API")
                                .summary("보고서 조회 API")
                                .pathParameters(
                                        List.of(
                                                parameterWithName("counselId")
                                                        .description("상담 아이디")
                                        )
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("학생 어세스 토큰")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.studentInfo").type(OBJECT)
                                                        .description("학생 정보"),
                                                fieldWithPath("body.studentInfo.name").type(STRING)
                                                        .description("학생 이름"),
                                                fieldWithPath("body.studentInfo.schoolName").type(STRING)
                                                        .description("학교 이름"),
                                                fieldWithPath("body.studentInfo.grade").type(NUMBER)
                                                        .description("학년"),
                                                fieldWithPath("body.teacherInfo").type(OBJECT)
                                                        .description("담당교사 정보"),
                                                fieldWithPath("body.teacherInfo.name").type(STRING)
                                                        .description("교사 이름"),
                                                fieldWithPath("body.teacherInfo.schoolName").type(STRING)
                                                        .description("학교 이름"),
                                                fieldWithPath("body.counselId").type(NUMBER)
                                                        .description("상담 아이디"),
                                                fieldWithPath("body.counselDate").type(STRING)
                                                        .description("상담 시작일"),
                                                fieldWithPath("body.counselStep").type(STRING)
                                                        .description("상담 단계"),
                                                fieldWithPath("body.personality").type(STRING)
                                                        .description("성향 요약"),
                                                fieldWithPath("body.selfAwareness").type(STRING)
                                                        .description("자기 이해도"),
                                                fieldWithPath("body.selfAwarenessDescription").type(STRING)
                                                        .description("자기 이해도 설명"),
                                                fieldWithPath("body.strengthSummary").type(STRING)
                                                        .description("강점 요약"),
                                                fieldWithPath("body.weaknessSummary").type(STRING)
                                                        .description("약점 요약"),
                                                fieldWithPath("body.strengths").type(ARRAY)
                                                        .description("강점 리스트"),
                                                fieldWithPath("body.weaknesses").type(ARRAY)
                                                        .description("약점 리스트"),
                                                fieldWithPath("body.interests").type(ARRAY)
                                                        .description("흥미 리스트"),
                                                fieldWithPath("body.jobRecommendations").type(ARRAY)
                                                        .description("추천 직업 리스트"),
                                                fieldWithPath("body.jobRecommendations[].id").type(NUMBER)
                                                        .description("직업 아이디"),
                                                fieldWithPath("body.jobRecommendations[].name").type(STRING)
                                                        .description("직업 이름"),
                                                fieldWithPath("body.jobRecommendations[].recommendReason").type(STRING)
                                                        .description("직업 추천 이유"),
                                                fieldWithPath("body.jobRecommendations[].growthSuggestion").type(STRING)
                                                        .description("직업 성장 제안"),
                                                fieldWithPath("body.jobRecommendations[].requiredSkills").type(ARRAY)
                                                        .description("필요 역량"),
                                                fieldWithPath("body.growthSuggestions").type(ARRAY)
                                                        .description("성장 제안 리스트"),
                                                fieldWithPath("body.teacherFeedback").type(OBJECT)
                                                        .description("교사 피드백"),
                                                fieldWithPath("body.teacherFeedback.comment").type(STRING)
                                                        .description("담당교사 한마디"),
                                                fieldWithPath("body.teacherFeedback.jobSuggestion").type(STRING)
                                                        .description("담당교사 직업 추천"),
                                                fieldWithPath("body.teacherFeedback.guidance").type(ARRAY)
                                                        .description("담당교사 성장 지도"),
                                                fieldWithPath("body.teacherFeedback.guidance[].seq").type(NUMBER)
                                                        .description("번호"),
                                                fieldWithPath("body.teacherFeedback.guidance[].content").type(STRING)
                                                        .description("내용")
                                        )
                                )
                                .requestSchema(Schema.schema("보고서 조회 (학생 , 최종) Request"))
                                .responseSchema(Schema.schema("보고서 조회 (학생 , 최종) Response"))
                                .build()
                        ))
                );
    }

    private String getReportContent() throws Exception{

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("personality", "이 학생은 사람과 소통하는 것을 좋아하고, 팀워크를 중시하는 성향을 보였습니다.  창의적인 아이디어를 제안하는 데 흥미를 느끼며, 반복적인 작업에는 흥미를 느끼지 못하는 경향이 있습니다.  이러한 성향을 바탕으로 커뮤니케이션 능력과 창의성을 활용할 수 있는 진로를 추천했습니다.");
        jsonObject.put("selfAwareness", "MIDDLE");
        jsonObject.put("selfAwarenessDescription", "자신의 강점과 관심사를 잘 인식하고 있으며, 이를 진로 방향성과 연결하는 능력이 뛰어납니다. 다만 구체적인 계획 수립 단계에서는 약간의 추가 고민이 필요할 수 있습니다.");
        jsonObject.put("strengthSummary", "이 학생은 사람과 소통하는 것을 좋아하고, 팀워크를 중시하는 성향을 보였습니다.");
        jsonObject.put("weaknessSummary", "창의적인 아이디어를 제안하는 데 흥미를 느끼며, 반복적인 작업에는 흥미를 느끼지 못하는 경향이 있습니다.");

        jsonObject.put("strengths", new JSONArray(List.of("팀워크", "창의적 사고", "커뮤니케이션 능력")));
        jsonObject.put("weaknesses", new JSONArray(List.of("반복 작업에 대한 낮은 흥미", "세부적인 규칙 준수에 대한 집중력 저하")));
        jsonObject.put("interests", new JSONArray(List.of("마케팅", "브랜딩 기획", "교육 콘텐츠 개발")));
        jsonObject.put("growthSuggestions", new JSONArray(List.of(
                "교내 홍보 및 마케팅 활동에 참여하여 실전 경험 쌓기",
                "다양한 주제에 대한 발표 및 아이디어 제안 활동 강화", "창의적 사고를 키우기 위해 프로젝트 기반 학습에 적극적으로 참여하기"
        )));
        jsonObject.put("jobSuggestions", new JSONArray(List.of(1068, 1103)));
        jsonObject.put("jobSuggestionReasons", new JSONArray(List.of(
                "소통 능력과 창의적인 아이디어 발휘 역량을 활용하여 브랜드와 서비스를 효과적으로 알릴 수 있습니다.",
                "소통 능력과 창의적인 아이디어 발휘 역량을 활용하여 브랜드와 서비스를 효과적으로 알릴 수 있습니다."
        )));
        jsonObject.put("jobGrowthSuggestions", new JSONArray(List.of(
                "1. 다양한 브랜드 캠페인을 분석하며 기획 아이디어를 떠올려보세요.  2. 사람들의 소비 트렌드를 관심 있게 관찰해보세요.  3. 친구들과 팀을 이루어 작은 프로젝트를 기획하고 실행해보세요.",
                "1. 다양한 콘텐츠(영상, 글, 카드뉴스)를 분석하며 좋은 포인트를 기록해보세요.  2. 관심 있는 주제로 직접 짧은 콘텐츠를 기획하고 만들어보세요.  3. 다른 사람의 피드백을 받아 콘텐츠를 개선하는 연습을 해보세요."
        )));

        return jsonObject.toString();
    }

    private String getWrongReportContent() throws Exception{

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("personality", "이 학생은 사람과 소통하는 것을 좋아하고, 팀워크를 중시하는 성향을 보였습니다.  창의적인 아이디어를 제안하는 데 흥미를 느끼며, 반복적인 작업에는 흥미를 느끼지 못하는 경향이 있습니다.  이러한 성향을 바탕으로 커뮤니케이션 능력과 창의성을 활용할 수 있는 진로를 추천했습니다.");
        jsonObject.put("selfAwareness", "MIDDLE");
        jsonObject.put("selfAwarenessDescription", "자신의 강점과 관심사를 잘 인식하고 있으며, 이를 진로 방향성과 연결하는 능력이 뛰어납니다. 다만 구체적인 계획 수립 단계에서는 약간의 추가 고민이 필요할 수 있습니다.");
        jsonObject.put("strengthSummary", "이 학생은 사람과 소통하는 것을 좋아하고, 팀워크를 중시하는 성향을 보였습니다.");
        jsonObject.put("weaknessSummary", "창의적인 아이디어를 제안하는 데 흥미를 느끼며, 반복적인 작업에는 흥미를 느끼지 못하는 경향이 있습니다.");

        jsonObject.put("strengths", new JSONArray(List.of("팀워크", "창의적 사고", "커뮤니케이션 능력")));
        jsonObject.put("weaknesses", new JSONArray(List.of("반복 작업에 대한 낮은 흥미", "세부적인 규칙 준수에 대한 집중력 저하")));
        jsonObject.put("interests", new JSONArray(List.of("마케팅", "브랜딩 기획", "교육 콘텐츠 개발")));
        jsonObject.put("growthSuggestions", new JSONArray(List.of(
                "교내 홍보 및 마케팅 활동에 참여하여 실전 경험 쌓기",
                "다양한 주제에 대한 발표 및 아이디어 제안 활동 강화", "창의적 사고를 키우기 위해 프로젝트 기반 학습에 적극적으로 참여하기"
        )));
        jsonObject.put("jobSuggestions", new JSONArray(List.of(1068)));
        jsonObject.put("jobSuggestionReasons", new JSONArray(List.of(
                "소통 능력과 창의적인 아이디어 발휘 역량을 활용하여 브랜드와 서비스를 효과적으로 알릴 수 있습니다.",
                "소통 능력과 창의적인 아이디어 발휘 역량을 활용하여 브랜드와 서비스를 효과적으로 알릴 수 있습니다."
        )));
        jsonObject.put("jobGrowthSuggestions", new JSONArray(List.of(
                "1. 다양한 브랜드 캠페인을 분석하며 기획 아이디어를 떠올려보세요.  2. 사람들의 소비 트렌드를 관심 있게 관찰해보세요.  3. 친구들과 팀을 이루어 작은 프로젝트를 기획하고 실행해보세요.",
                "1. 다양한 콘텐츠(영상, 글, 카드뉴스)를 분석하며 좋은 포인트를 기록해보세요.  2. 관심 있는 주제로 직접 짧은 콘텐츠를 기획하고 만들어보세요.  3. 다른 사람의 피드백을 받아 콘텐츠를 개선하는 연습을 해보세요."
        )));

        return jsonObject.toString();
    }

    private void createReport() throws Exception{

        Long counselId = 6L;
        String content = getReportContent();

        mockMvc.perform(
                post("/api/counsels/{counselId}/reports", counselId)
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .characterEncoding("UTF-8")
        );

    }
}
