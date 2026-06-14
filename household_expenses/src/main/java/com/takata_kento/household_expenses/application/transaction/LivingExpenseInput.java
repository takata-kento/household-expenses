package com.takata_kento.household_expenses.application.transaction;

import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.LivingExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.Money;

public record LivingExpenseInput(LivingExpenseCategoryId categoryId, Money amount, Description memo) {}
