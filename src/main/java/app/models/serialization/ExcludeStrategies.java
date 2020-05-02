package app.models.serialization;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

public class ExcludeStrategies {

    public static final ExclusionStrategy MANY_TO_ONE = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            return field.getAnnotation(ManyToOne.class) != null;
        }
    };

    public static final ExclusionStrategy ONE_TO_MANY = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            return field.getAnnotation(OneToMany.class) != null;
        }
    };

    public static final ExclusionStrategy ONE_TO_MANY_AND_MANY_TO_ONE = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            return (field.getAnnotation(OneToMany.class) != null) || (field.getAnnotation(ManyToOne.class) != null);
        }
    };

    public static final ExclusionStrategy ONE_TO_ONE = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            return field.getAnnotation(OneToOne.class) != null;
        }
    };

    public static final ExclusionStrategy EXCLUDE_BDOCS = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            return field.getAnnotation(ExcludeForBDocs.class) != null;
        }
    };
}
