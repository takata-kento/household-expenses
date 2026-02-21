package com.takata_kento.household_expenses.config;

import com.takata_kento.household_expenses.domain.valueobject.AccountName;
import com.takata_kento.household_expenses.domain.valueobject.BalanceEditHistoryId;
import com.takata_kento.household_expenses.domain.valueobject.CategoryName;
import com.takata_kento.household_expenses.domain.valueobject.DailyGroupTransactionId;
import com.takata_kento.household_expenses.domain.valueobject.FinancialAccountId;
import com.takata_kento.household_expenses.domain.valueobject.DailyLivingExpenseId;
import com.takata_kento.household_expenses.domain.valueobject.DailyPersonalExpenseId;
import com.takata_kento.household_expenses.domain.valueobject.DailyPersonalTransactionId;
import com.takata_kento.household_expenses.domain.valueobject.Day;
import com.takata_kento.household_expenses.domain.valueobject.Description;
import com.takata_kento.household_expenses.domain.valueobject.FixedExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.GroupInvitationId;
import com.takata_kento.household_expenses.domain.valueobject.GroupName;
import com.takata_kento.household_expenses.domain.valueobject.LivingExpenseCategoryId;
import com.takata_kento.household_expenses.domain.valueobject.Money;
import com.takata_kento.household_expenses.domain.valueobject.Month;
import com.takata_kento.household_expenses.domain.valueobject.MonthlyBudgetId;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Username;
import com.takata_kento.household_expenses.domain.valueobject.Year;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;

@Configuration
class SpringDataJdbcConfiguration extends AbstractJdbcConfiguration {

    @Override
    protected List<?> userConverters() {
        return Arrays.asList(
            new UserIdToStringConverter(),
            new StringToUserIdConverter(),
            new UsernameToStringConverter(),
            new StringToUsernameConverter(),
            new UserGroupIdToStringConverter(),
            new StringToUserGroupIdConverter(),
            new GroupInvitationIdToStringConverter(),
            new StringToGroupInvitationIdConverter(),
            new OptionalUserGroupIdToStringConverter(),
            new StringToOptionalUserGroupIdConverter(),
            new GroupNameToStringConverter(),
            new StringToGroupNameConverter(),
            new DayToIntegerConverter(),
            new IntegerToDayConverter(),
            new MonthlyBudgetIdToStringConverter(),
            new StringToMonthlyBudgetIdConverter(),
            new YearToIntegerConverter(),
            new IntegerToYearConverter(),
            new MonthToIntegerConverter(),
            new IntegerToMonthConverter(),
            new MoneyToIntegerConverter(),
            new IntegerToMoneyConverter(),
            new LivingExpenseCategoryIdToStringConverter(),
            new StringToLivingExpenseCategoryIdConverter(),
            new CategoryNameToStringConverter(),
            new StringToCategoryNameConverter(),
            new DescriptionToStringConverter(),
            new StringToDescriptionConverter(),
            new DailyLivingExpenseIdToStringConverter(),
            new StringToDailyLivingExpenseIdConverter(),
            new DailyGroupTransactionIdToStringConverter(),
            new StringToDailyGroupTransactionIdConverter(),
            new DailyPersonalTransactionIdToStringConverter(),
            new StringToDailyPersonalTransactionIdConverter(),
            new DailyPersonalExpenseIdToStringConverter(),
            new StringToDailyPersonalExpenseIdConverter(),
            new FixedExpenseCategoryIdToStringConverter(),
            new StringToFixedExpenseCategoryIdConverter(),
            new FinancialAccountIdToStringConverter(),
            new StringToFinancialAccountIdConverter(),
            new AccountNameToStringConverter(),
            new StringToAccountNameConverter(),
            new BalanceEditHistoryIdToStringConverter(),
            new StringToBalanceEditHistoryIdConverter()
        );
    }

    @WritingConverter
    static class UserIdToStringConverter implements Converter<UserId, String> {

        @Override
        public String convert(UserId source) {
            return source.toString();
        }
    }

    @ReadingConverter
    static class StringToUserIdConverter implements Converter<String, UserId> {

        @Override
        public UserId convert(String source) {
            return new UserId(UUID.fromString(source));
        }
    }

    @WritingConverter
    static class UsernameToStringConverter implements Converter<Username, String> {

        @Override
        public String convert(Username source) {
            return source.value();
        }
    }

    @ReadingConverter
    static class StringToUsernameConverter implements Converter<String, Username> {

        @Override
        public Username convert(String source) {
            return new Username(source);
        }
    }

    @WritingConverter
    static class UserGroupIdToStringConverter implements Converter<UserGroupId, String> {

        @Override
        public String convert(UserGroupId source) {
            return source.toString();
        }
    }

    @ReadingConverter
    static class StringToUserGroupIdConverter implements Converter<String, UserGroupId> {

        @Override
        public UserGroupId convert(String source) {
            return new UserGroupId(UUID.fromString(source));
        }
    }

    @WritingConverter
    static class GroupInvitationIdToStringConverter implements Converter<GroupInvitationId, String> {

        @Override
        public String convert(GroupInvitationId source) {
            return source.toString();
        }
    }

    @ReadingConverter
    static class StringToGroupInvitationIdConverter implements Converter<String, GroupInvitationId> {

        @Override
        public GroupInvitationId convert(String source) {
            return new GroupInvitationId(UUID.fromString(source));
        }
    }

    @WritingConverter
    static class OptionalUserGroupIdToStringConverter implements Converter<Optional<UserGroupId>, String> {

        @Override
        public String convert(Optional<UserGroupId> source) {
            return source.map(UserGroupId::toString).orElse(null);
        }
    }

    @ReadingConverter
    static class StringToOptionalUserGroupIdConverter implements Converter<String, Optional<UserGroupId>> {

        @Override
        public Optional<UserGroupId> convert(String source) {
            return source != null ? Optional.of(new UserGroupId(UUID.fromString(source))) : Optional.empty();
        }
    }

    @WritingConverter
    static class GroupNameToStringConverter implements Converter<GroupName, String> {

        @Override
        public String convert(GroupName source) {
            return source.value();
        }
    }

    @ReadingConverter
    static class StringToGroupNameConverter implements Converter<String, GroupName> {

        @Override
        public GroupName convert(String source) {
            return new GroupName(source);
        }
    }

    @WritingConverter
    static class DayToIntegerConverter implements Converter<Day, Integer> {

        @Override
        public Integer convert(Day source) {
            return source.value();
        }
    }

    @ReadingConverter
    static class IntegerToDayConverter implements Converter<Integer, Day> {

        @Override
        public Day convert(Integer source) {
            return new Day(source);
        }
    }

    @WritingConverter
    static class MonthlyBudgetIdToStringConverter implements Converter<MonthlyBudgetId, String> {

        @Override
        public String convert(MonthlyBudgetId source) {
            return source.toString();
        }
    }

    @ReadingConverter
    static class StringToMonthlyBudgetIdConverter implements Converter<String, MonthlyBudgetId> {

        @Override
        public MonthlyBudgetId convert(String source) {
            return new MonthlyBudgetId(UUID.fromString(source));
        }
    }

    @WritingConverter
    static class YearToIntegerConverter implements Converter<Year, Integer> {

        @Override
        public Integer convert(Year source) {
            return source.value();
        }
    }

    @ReadingConverter
    static class IntegerToYearConverter implements Converter<Integer, Year> {

        @Override
        public Year convert(Integer source) {
            return new Year(source);
        }
    }

    @WritingConverter
    static class MonthToIntegerConverter implements Converter<Month, Integer> {

        @Override
        public Integer convert(Month source) {
            return source.value();
        }
    }

    @ReadingConverter
    static class IntegerToMonthConverter implements Converter<Integer, Month> {

        @Override
        public Month convert(Integer source) {
            return new Month(source);
        }
    }

    @WritingConverter
    static class MoneyToIntegerConverter implements Converter<Money, Integer> {

        @Override
        public Integer convert(Money source) {
            return source.amount();
        }
    }

    @ReadingConverter
    static class IntegerToMoneyConverter implements Converter<Integer, Money> {

        @Override
        public Money convert(Integer source) {
            return new Money(source);
        }
    }

    @WritingConverter
    static class LivingExpenseCategoryIdToStringConverter implements Converter<LivingExpenseCategoryId, String> {

        @Override
        public String convert(LivingExpenseCategoryId source) {
            return source.toString();
        }
    }

    @ReadingConverter
    static class StringToLivingExpenseCategoryIdConverter implements Converter<String, LivingExpenseCategoryId> {

        @Override
        public LivingExpenseCategoryId convert(String source) {
            return new LivingExpenseCategoryId(UUID.fromString(source));
        }
    }

    @WritingConverter
    static class CategoryNameToStringConverter implements Converter<CategoryName, String> {

        @Override
        public String convert(CategoryName source) {
            return source.value();
        }
    }

    @ReadingConverter
    static class StringToCategoryNameConverter implements Converter<String, CategoryName> {

        @Override
        public CategoryName convert(String source) {
            return new CategoryName(source);
        }
    }

    @WritingConverter
    static class DescriptionToStringConverter implements Converter<Description, String> {

        @Override
        public String convert(Description source) {
            return source.value();
        }
    }

    @ReadingConverter
    static class StringToDescriptionConverter implements Converter<String, Description> {

        @Override
        public Description convert(String source) {
            return new Description(source);
        }
    }

    @WritingConverter
    static class DailyLivingExpenseIdToStringConverter implements Converter<DailyLivingExpenseId, String> {

        @Override
        public String convert(DailyLivingExpenseId source) {
            return source.toString();
        }
    }

    @ReadingConverter
    static class StringToDailyLivingExpenseIdConverter implements Converter<String, DailyLivingExpenseId> {

        @Override
        public DailyLivingExpenseId convert(String source) {
            return new DailyLivingExpenseId(UUID.fromString(source));
        }
    }

    @WritingConverter
    static class DailyGroupTransactionIdToStringConverter implements Converter<DailyGroupTransactionId, String> {

        @Override
        public String convert(DailyGroupTransactionId source) {
            return source.toString();
        }
    }

    @ReadingConverter
    static class StringToDailyGroupTransactionIdConverter implements Converter<String, DailyGroupTransactionId> {

        @Override
        public DailyGroupTransactionId convert(String source) {
            return new DailyGroupTransactionId(UUID.fromString(source));
        }
    }

    @WritingConverter
    static class DailyPersonalTransactionIdToStringConverter implements Converter<DailyPersonalTransactionId, String> {

        @Override
        public String convert(DailyPersonalTransactionId source) {
            return source.toString();
        }
    }

    @ReadingConverter
    static class StringToDailyPersonalTransactionIdConverter implements Converter<String, DailyPersonalTransactionId> {

        @Override
        public DailyPersonalTransactionId convert(String source) {
            return new DailyPersonalTransactionId(UUID.fromString(source));
        }
    }

    @WritingConverter
    static class DailyPersonalExpenseIdToStringConverter implements Converter<DailyPersonalExpenseId, String> {

        @Override
        public String convert(DailyPersonalExpenseId source) {
            return source.toString();
        }
    }

    @ReadingConverter
    static class StringToDailyPersonalExpenseIdConverter implements Converter<String, DailyPersonalExpenseId> {

        @Override
        public DailyPersonalExpenseId convert(String source) {
            return new DailyPersonalExpenseId(UUID.fromString(source));
        }
    }

    @WritingConverter
    static class FixedExpenseCategoryIdToStringConverter implements Converter<FixedExpenseCategoryId, String> {

        @Override
        public String convert(FixedExpenseCategoryId source) {
            return source.toString();
        }
    }

    @ReadingConverter
    static class StringToFixedExpenseCategoryIdConverter implements Converter<String, FixedExpenseCategoryId> {

        @Override
        public FixedExpenseCategoryId convert(String source) {
            return new FixedExpenseCategoryId(UUID.fromString(source));
        }
    }

    @WritingConverter
    static class FinancialAccountIdToStringConverter implements Converter<FinancialAccountId, String> {

        @Override
        public String convert(FinancialAccountId source) {
            return source.toString();
        }
    }

    @ReadingConverter
    static class StringToFinancialAccountIdConverter implements Converter<String, FinancialAccountId> {

        @Override
        public FinancialAccountId convert(String source) {
            return new FinancialAccountId(UUID.fromString(source));
        }
    }

    @WritingConverter
    static class AccountNameToStringConverter implements Converter<AccountName, String> {

        @Override
        public String convert(AccountName source) {
            return source.value();
        }
    }

    @ReadingConverter
    static class StringToAccountNameConverter implements Converter<String, AccountName> {

        @Override
        public AccountName convert(String source) {
            return new AccountName(source);
        }
    }

    @WritingConverter
    static class BalanceEditHistoryIdToStringConverter implements Converter<BalanceEditHistoryId, String> {

        @Override
        public String convert(BalanceEditHistoryId source) {
            return source.toString();
        }
    }

    @ReadingConverter
    static class StringToBalanceEditHistoryIdConverter implements Converter<String, BalanceEditHistoryId> {

        @Override
        public BalanceEditHistoryId convert(String source) {
            return new BalanceEditHistoryId(UUID.fromString(source));
        }
    }
}
