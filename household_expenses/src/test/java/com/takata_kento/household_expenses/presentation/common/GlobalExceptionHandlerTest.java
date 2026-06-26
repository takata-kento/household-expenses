package com.takata_kento.household_expenses.presentation.common;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.takata_kento.household_expenses.application.exception.ConflictException;
import com.takata_kento.household_expenses.application.exception.ForbiddenException;
import com.takata_kento.household_expenses.application.exception.GroupMembershipRequiredException;
import com.takata_kento.household_expenses.application.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@link GlobalExceptionHandler} の例外→HTTPステータス変換と、内部メッセージを露出しないことを検証する。
 * スタブコントローラに各例外を送出させて確認する。
 */
class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new StubController())
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
    }

    @Test
    void testResourceNotFoundReturns404WithoutLeakingMessage() throws Exception {
        // When / Then
        mockMvc
            .perform(get("/stub/not-found"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("指定されたリソースが見つかりません。"));
    }

    @Test
    void testForbiddenReturns403WithoutLeakingMessage() throws Exception {
        // When / Then
        mockMvc
            .perform(get("/stub/forbidden"))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message").value("この操作を行う権限がありません。"));
    }

    @Test
    void testConflictReturns409WithoutLeakingMessage() throws Exception {
        // When / Then
        mockMvc
            .perform(get("/stub/conflict"))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.message").value("リクエストが現在のリソースの状態と競合しています。"));
    }

    @Test
    void testGroupMembershipRequiredReturns400() throws Exception {
        // When / Then
        mockMvc
            .perform(get("/stub/group-required"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("リクエストの内容が不正です。"));
    }

    @Test
    void testIllegalStateReturns500WithoutLeakingMessage() throws Exception {
        // When / Then
        mockMvc
            .perform(get("/stub/illegal-state"))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.message").value("サーバー内部でエラーが発生しました。"));
    }

    @Test
    void testTypeMismatchReturns400() throws Exception {
        // When / Then
        mockMvc.perform(get("/stub/number/not-a-number")).andExpect(status().isBadRequest());
    }

    @Test
    void testUnreadableBodyReturns400() throws Exception {
        // When / Then
        mockMvc
            .perform(post("/stub/body").contentType(MediaType.APPLICATION_JSON).content("{ broken json "))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("リクエストボディの形式が不正です。"));
    }

    @RestController
    static class StubController {

        @GetMapping("/stub/not-found")
        void notFound() {
            throw new ResourceNotFoundException("FinancialAccount not found: 1234567");
        }

        @GetMapping("/stub/forbidden")
        void forbidden() {
            throw new ForbiddenException("FinancialAccount is not owned by the current user");
        }

        @GetMapping("/stub/conflict")
        void conflict() {
            throw new ConflictException("Account number is already used by another user: 1234567");
        }

        @GetMapping("/stub/group-required")
        void groupRequired() {
            throw new GroupMembershipRequiredException("User does not belong to any group");
        }

        @GetMapping("/stub/illegal-state")
        void illegalState() {
            throw new IllegalStateException("User not found: internal-uuid");
        }

        @GetMapping("/stub/number/{value}")
        void number(@PathVariable int value) {
            // 到達しない: int への変換失敗で MethodArgumentTypeMismatchException が発生する
        }

        @PostMapping("/stub/body")
        void body(@RequestBody StubBody requestBody) {
            // 到達しない: 不正なJSONで HttpMessageNotReadableException が発生する
        }
    }

    record StubBody(String name) {}
}
