package com.takata_kento.household_expenses.config;

import com.takata_kento.household_expenses.domain.valueobject.Day;
import com.takata_kento.household_expenses.domain.valueobject.GroupInvitationId;
import com.takata_kento.household_expenses.domain.valueobject.GroupName;
import com.takata_kento.household_expenses.domain.valueobject.UserGroupId;
import com.takata_kento.household_expenses.domain.valueobject.UserId;
import com.takata_kento.household_expenses.domain.valueobject.Username;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.lang.NonNull;

@Configuration
class SpringDataJdbcConfiguration extends AbstractJdbcConfiguration {

    @Override
    @NonNull
    protected List<?> userConverters() {
        return Arrays.asList(
            new UserIdToLongConverter(),
            new LongToUserIdConverter(),
            new UsernameToStringConverter(),
            new StringToUsernameConverter(),
            new UserGroupIdToLongConverter(),
            new LongToUserGroupIdConverter(),
            new GroupInvitationIdToLongConverter(),
            new LongToGroupInvitationIdConverter(),
            new OptionalUserGroupIdToLongConverter(),
            new LongToOptionalUserGroupIdConverter(),
            new GroupNameToStringConverter(),
            new StringToGroupNameConverter(),
            new DayToIntegerConverter(),
            new IntegerToDayConverter()
        );
    }

    @WritingConverter
    static class UserIdToLongConverter implements Converter<UserId, Long> {

        @Override
        public Long convert(@NonNull UserId source) {
            return source.value();
        }
    }

    @ReadingConverter
    static class LongToUserIdConverter implements Converter<Long, UserId> {

        @Override
        public UserId convert(@NonNull Long source) {
            return new UserId(source);
        }
    }

    @WritingConverter
    static class UsernameToStringConverter implements Converter<Username, String> {

        @Override
        public String convert(@NonNull Username source) {
            return source.value();
        }
    }

    @ReadingConverter
    static class StringToUsernameConverter implements Converter<String, Username> {

        @Override
        public Username convert(@NonNull String source) {
            return new Username(source);
        }
    }

    @WritingConverter
    static class UserGroupIdToLongConverter implements Converter<UserGroupId, Long> {

        @Override
        public Long convert(@NonNull UserGroupId source) {
            return source.value();
        }
    }

    @ReadingConverter
    static class LongToUserGroupIdConverter implements Converter<Long, UserGroupId> {

        @Override
        public UserGroupId convert(@NonNull Long source) {
            return new UserGroupId(source);
        }
    }

    @WritingConverter
    static class GroupInvitationIdToLongConverter implements Converter<GroupInvitationId, Long> {

        @Override
        public Long convert(@NonNull GroupInvitationId source) {
            return source.value();
        }
    }

    @ReadingConverter
    static class LongToGroupInvitationIdConverter implements Converter<Long, GroupInvitationId> {

        @Override
        public GroupInvitationId convert(@NonNull Long source) {
            return new GroupInvitationId(source);
        }
    }

    @WritingConverter
    static class OptionalUserGroupIdToLongConverter implements Converter<Optional<UserGroupId>, Long> {

        @Override
        public Long convert(@NonNull Optional<UserGroupId> source) {
            return source.map(UserGroupId::value).orElse(null);
        }
    }

    @ReadingConverter
    static class LongToOptionalUserGroupIdConverter implements Converter<Long, Optional<UserGroupId>> {

        @Override
        public Optional<UserGroupId> convert(@NonNull Long source) {
            return source != null ? Optional.of(new UserGroupId(source)) : Optional.empty();
        }
    }

    @WritingConverter
    static class GroupNameToStringConverter implements Converter<GroupName, String> {

        @Override
        public String convert(@NonNull GroupName source) {
            return source.value();
        }
    }

    @ReadingConverter
    static class StringToGroupNameConverter implements Converter<String, GroupName> {

        @Override
        public GroupName convert(@NonNull String source) {
            return new GroupName(source);
        }
    }

    @WritingConverter
    static class DayToIntegerConverter implements Converter<Day, Integer> {

        @Override
        public Integer convert(@NonNull Day source) {
            return source.value();
        }
    }

    @ReadingConverter
    static class IntegerToDayConverter implements Converter<Integer, Day> {

        @Override
        public Day convert(@NonNull Integer source) {
            return new Day(source);
        }
    }
}
