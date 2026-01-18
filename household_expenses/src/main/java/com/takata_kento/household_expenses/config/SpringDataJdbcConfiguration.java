package com.takata_kento.household_expenses.config;

import com.takata_kento.household_expenses.domain.valueobject.CategoryName;
import com.takata_kento.household_expenses.domain.valueobject.DailyGroupTransactionId;
import com.takata_kento.household_expenses.domain.valueobject.DailyLivingExpenseId;
import com.takata_kento.household_expenses.domain.valueobject.DailyPersonalExpenseId;
import com.takata_kento.household_expenses.domain.valueobject.DailyPersonalTransactionId;
import com.takata_kento.household_expenses.domain.valueobject.Day;
import com.takata_kento.household_expenses.domain.valueobject.Description;
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
            new UserGroupIdToLongConverter(),
            new LongToUserGroupIdConverter(),
            new GroupInvitationIdToStringConverter(),
            new StringToGroupInvitationIdConverter(),
            new OptionalUserGroupIdToLongConverter(),
            new LongToOptionalUserGroupIdConverter(),
            new GroupNameToStringConverter(),
            new StringToGroupNameConverter(),
            new DayToIntegerConverter(),
            new IntegerToDayConverter(),
            new MonthlyBudgetIdToLongConverter(),
            new LongToMonthlyBudgetIdConverter(),
            new YearToIntegerConverter(),
            new IntegerToYearConverter(),
            new MonthToIntegerConverter(),
            new IntegerToMonthConverter(),
            new MoneyToIntegerConverter(),
            new IntegerToMoneyConverter(),
            new LivingExpenseCategoryIdToLongConverter(),
            new LongToLivingExpenseCategoryIdConverter(),
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
            new StringToDailyPersonalExpenseIdConverter()
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
    static class UserGroupIdToLongConverter implements Converter<UserGroupId, Long> {

        @Override
        public Long convert(UserGroupId source) {
            return source.value();
        }
    }

    @ReadingConverter
    static class LongToUserGroupIdConverter implements Converter<Long, UserGroupId> {

        @Override
        public UserGroupId convert(Long source) {
            return new UserGroupId(source);
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
    static class OptionalUserGroupIdToLongConverter implements Converter<Optional<UserGroupId>, Long> {

        @Override
        public Long convert(Optional<UserGroupId> source) {
            return source.map(UserGroupId::value).orElse(null);
        }
    }

    @ReadingConverter
    static class LongToOptionalUserGroupIdConverter implements Converter<Long, Optional<UserGroupId>> {

        @Override
        public Optional<UserGroupId> convert(Long source) {
            return source != null ? Optional.of(new UserGroupId(source)) : Optional.empty();
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
    static class MonthlyBudgetIdToLongConverter implements Converter<MonthlyBudgetId, Long> {

        @Override
        public Long convert(MonthlyBudgetId source) {
            return source.value();
        }
    }

    @ReadingConverter
    static class LongToMonthlyBudgetIdConverter implements Converter<Long, MonthlyBudgetId> {

        @Override
        public MonthlyBudgetId convert(Long source) {
            return new MonthlyBudgetId(source);
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
    static class LivingExpenseCategoryIdToLongConverter implements Converter<LivingExpenseCategoryId, Long> {

        @Override
        public Long convert(LivingExpenseCategoryId source) {
            return source.value();
        }
    }

    @ReadingConverter
    static class LongToLivingExpenseCategoryIdConverter implements Converter<Long, LivingExpenseCategoryId> {

        @Override
        public LivingExpenseCategoryId convert(Long source) {
            return new LivingExpenseCategoryId(source);
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
}
