package com.takata_kento.household_expenses.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.test.context.support.WithSecurityContext;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCognitoUserSecurityContextFactory.class)
public @interface WithMockCognitoUser {

    /** Cognito の sub クレーム（UserId に対応する UUID 文字列） */
    String sub() default "00000000-0000-0000-0000-000000000001";

    /** Cognito グループ名（ROLE_ プレフィックスなし） */
    String[] groups() default {"USER"};
}
