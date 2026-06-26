package com.takata_kento.household_expenses.presentation.common;

import com.takata_kento.household_expenses.application.exception.ConflictException;
import com.takata_kento.household_expenses.application.exception.ForbiddenException;
import com.takata_kento.household_expenses.application.exception.GroupMembershipRequiredException;
import com.takata_kento.household_expenses.application.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 各 Controller で発生するアプリケーション例外を HTTP レスポンスへ変換する共通ハンドラ。
 *
 * <p>例外型ごとに適切なステータスコードへ振り分ける。
 *
 * <ul>
 *   <li>{@link ResourceNotFoundException} → 404 Not Found</li>
 *   <li>{@link ForbiddenException}（所有権・認可違反） → 403 Forbidden</li>
 *   <li>{@link ConflictException}（重複登録などの状態競合） → 409 Conflict</li>
 *   <li>{@link GroupMembershipRequiredException}（グループ未所属） / バリデーション失敗 → 400 Bad Request</li>
 *   <li>その他の想定外例外 → 500 Internal Server Error</li>
 * </ul>
 *
 * <p>情報漏えいを防ぐため、4xx/5xx のうち内部メッセージを含み得るものはクライアントへ定型文を返し、
 * 詳細はサーバーログにのみ記録する。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String NOT_FOUND_MESSAGE = "指定されたリソースが見つかりません。";
    private static final String FORBIDDEN_MESSAGE = "この操作を行う権限がありません。";
    private static final String CONFLICT_MESSAGE = "リクエストが現在のリソースの状態と競合しています。";
    private static final String BAD_REQUEST_MESSAGE = "リクエストの内容が不正です。";
    private static final String INTERNAL_ERROR_MESSAGE = "サーバー内部でエラーが発生しました。";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException exception) {
        log.warn("Resource not found: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(NOT_FOUND_MESSAGE));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException exception) {
        log.warn("Forbidden access: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(FORBIDDEN_MESSAGE));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException exception) {
        log.warn("Conflict: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(CONFLICT_MESSAGE));
    }

    @ExceptionHandler(GroupMembershipRequiredException.class)
    public ResponseEntity<ErrorResponse> handleGroupMembershipRequired(GroupMembershipRequiredException exception) {
        log.warn("Group membership required: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(BAD_REQUEST_MESSAGE));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(BAD_REQUEST_MESSAGE));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .findFirst()
            .orElse(BAD_REQUEST_MESSAGE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new ErrorResponse("リクエストボディの形式が不正です。")
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException exception) {
        String message = "パラメータ '" + exception.getName() + "' の形式が不正です。";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception exception) {
        log.error("Unexpected error", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(INTERNAL_ERROR_MESSAGE));
    }
}
