package com.takata_kento.household_expenses.application.exception;

/**
 * クライアントが指定したリソースが存在しない場合に送出する例外。
 * HTTP では 404 Not Found に対応する。
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
