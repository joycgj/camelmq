package camel.tc.cmq;

import java.util.Collections;
import java.util.Set;

// done
public class SubscribeParam {
    private final boolean consumeMostOnce;
    private final TagType tagType;
    private boolean isBroadcast;
    private final Set<String> tags;

    private SubscribeParam(boolean consumeMostOnce, boolean isBroadcast, TagType tagType, Set<String> tags) {
        this.consumeMostOnce = consumeMostOnce;
        this.isBroadcast = isBroadcast;
        this.tags = tags;
        this.tagType = tagType;
    }

    public boolean isConsumeMostOnce() {
        return consumeMostOnce;
    }

    public Set<String> getTags() {
        return tags;
    }

    public TagType getTagType() {
        return tagType;
    }

    public boolean isBroadcast() {
        return isBroadcast;
    }

    public void setBroadcast(boolean isBroadcast) {
        this.isBroadcast = isBroadcast;
    }

    public static final class SubscribeParamBuilder {
        private boolean consumeMostOnce = false;
        private Set<String> tags = Collections.emptySet();
        private TagType tagType = TagType.NO_TAG;
        private boolean isBroadcast = false;

        public SubscribeParam create() {
            return new SubscribeParam(consumeMostOnce, isBroadcast, tagType, tags);
        }

        public SubscribeParamBuilder setConsumeMostOnce(boolean consumeMostOnce) {
            this.consumeMostOnce = consumeMostOnce;
            return this;
        }

        public SubscribeParamBuilder setTagType(final TagType tagType) {
            if (tagType != null) {
                this.tagType = tagType;
            }
            return this;
        }

        public SubscribeParamBuilder setTags(Set<String> tags) {
            if (tags != null && tags.size() != 0) {
                this.tags = tags;
            }
            return this;
        }

        public SubscribeParamBuilder setBroadcast(boolean isBroadcast) {
            this.isBroadcast = isBroadcast;
            return this;
        }
    }
}
