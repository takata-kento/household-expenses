package com.takata_kento.household_expenses.presentation.common;

/**
 * API のエラーレスポンスボディ。
 *
 * @param message エラー内容を表すメッセージ
 */
public record ErrorResponse(String message) {}
