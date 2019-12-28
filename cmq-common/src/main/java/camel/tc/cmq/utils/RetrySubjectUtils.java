package camel.tc.cmq.utils;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import java.util.List;

// done
public final class RetrySubjectUtils {
    private static final Joiner RETRY_SUBJECT_JOINER = Joiner.on('%');
    private static final Splitter RETRY_SUBJECT_SPLITTER = Splitter.on('%').trimResults().omitEmptyStrings();
    private static final String RETRY_SUBJECT_PREFIX = "%RETRY";
    private static final String DEAD_RETRY_SUBJECT_PREFIX = "%DEAD_RETRY";

    private RetrySubjectUtils() {
    }

    public static boolean isRealSubject(final String subject) {
        return !Strings.isNullOrEmpty(subject) && !isRetrySubject(subject) && !isDeadRetrySubject(subject);
    }

    public static String buildRetrySubject(final String subject, final String group) {
        return RETRY_SUBJECT_JOINER.join(RETRY_SUBJECT_PREFIX, subject, group);
    }

    public static boolean isRetrySubject(final String subject) {
        return Strings.nullToEmpty(subject).startsWith(RETRY_SUBJECT_PREFIX);
    }

    public static String buildDeadRetrySubject(final String subject, final String group) {
        return RETRY_SUBJECT_JOINER.join(DEAD_RETRY_SUBJECT_PREFIX, subject, group);
    }

    public static boolean isDeadRetrySubject(final String subject) {
        return Strings.nullToEmpty(subject).startsWith(DEAD_RETRY_SUBJECT_PREFIX);
    }

    public static String getRealSubject(final String subject) {
        final Optional<String> optional = getSubject(subject);
        if (optional.isPresent()) {
            return optional.get();
        }
        return subject;
    }

    public static Optional<String> getSubject(final String retrySubject) {
        if (!isRetrySubject(retrySubject) && !isDeadRetrySubject(retrySubject)) {
            return Optional.absent();
        }
        final List<String> parts = RETRY_SUBJECT_SPLITTER.splitToList(retrySubject);
        if (parts.size() != 3) {
            return Optional.absent();
        } else {
            return Optional.of(parts.get(1));
        }
    }

    public static String[] parseSubjectAndGroup(String subject) {
        if (!isRetrySubject(subject) && !isDeadRetrySubject(subject)) {
            return null;
        }

        final List<String> parts = RETRY_SUBJECT_SPLITTER.splitToList(subject);
        if (parts.size() != 3) {
            return null;
        } else {
            return new String[]{parts.get(1), parts.get(2)};
        }
    }

    public static String getConsumerGroup(final String subject) {
        if (!isDeadRetrySubject(subject) && !isRetrySubject(subject)) return "";
        final List<String> parts = RETRY_SUBJECT_SPLITTER.splitToList(subject);
        if (parts.size() != 3) return "";
        else return parts.get(2);
    }

    public static String buildRetrySubject(final String subject) {
        return RETRY_SUBJECT_JOINER.join(RETRY_SUBJECT_PREFIX, subject);
    }

    public static String buildDeadRetrySubject(final String subject) {
        return RETRY_SUBJECT_JOINER.join(DEAD_RETRY_SUBJECT_PREFIX, subject);
    }
}
