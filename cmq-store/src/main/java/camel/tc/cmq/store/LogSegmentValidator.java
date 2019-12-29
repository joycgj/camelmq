package camel.tc.cmq.store;

// done
public interface LogSegmentValidator {

    enum ValidateStatus {
        COMPLETE,
        PARTIAL
    }

    class ValidateResult {
        private final ValidateStatus status;
        private final int validatedSize;

        public ValidateResult(ValidateStatus status, int validatedSize) {
            this.status = status;
            this.validatedSize = validatedSize;
        }

        public ValidateStatus getStatus() {
            return status;
        }

        public int getValidatedSize() {
            return validatedSize;
        }
    }

    ValidateResult validate(final LogSegment segment);
}
