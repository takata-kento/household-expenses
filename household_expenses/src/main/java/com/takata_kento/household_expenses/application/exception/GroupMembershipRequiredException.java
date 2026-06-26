package com.takata_kento.household_expenses.application.exception;

/**
 * 操作にグループ所属が必要だが、ユーザーがどのグループにも所属していない場合に送出する例外。
 * グループ未所属は競合（409）ではなく、クライアントが解消すべき正常な前提条件の未充足であるため、
 * HTTP では 400 Bad Request に対応する。
 */
public class GroupMembershipRequiredException extends RuntimeException {

    public GroupMembershipRequiredException(String message) {
        super(message);
    }
}
