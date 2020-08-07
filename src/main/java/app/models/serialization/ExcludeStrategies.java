package app.models.serialization;

import app.security.models.annotations.ExcludeMatchingPassword;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import javax.persistence.ManyToMany;
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
            return (field.getAnnotation(OneToMany.class) != null)
                    ||
                    (field.getAnnotation(ManyToOne.class) != null);
        }
    };

    public static final ExclusionStrategy EXCLUDE_RELATIONS = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            return (field.getAnnotation(OneToMany.class) != null)
                    ||
                    (field.getAnnotation(ManyToOne.class) != null)
                    ||
                    (field.getAnnotation(ManyToMany.class) != null);
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

    public static final ExclusionStrategy EXCLUDE_MATCHING_PASSWORD = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            return field.getAnnotation(ExcludeMatchingPassword.class) != null;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    };

    public static final ExclusionStrategy EXLUDE_THIS = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            return field.getAnnotation(ExcludeThis.class) != null;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    };

    public static final ExclusionStrategy EXCLUDE_FOR_JSON_PERFORMER = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            return fieldAttributes.getAnnotation(ExcludeForJsonPerformer.class) != null;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    };

    public static final ExclusionStrategy EXCLUDE_FOR_COMMENT = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            return fieldAttributes.getAnnotation(ExcludeForJsonPerformer.class) != null
                    ||
                    fieldAttributes.getAnnotation(ExcludeForJsonComment.class) != null;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    };

    public static final ExclusionStrategy EXCLUDE_FOR_REPORT = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            return fieldAttributes.getAnnotation(ExcludeForJsonReport.class) != null;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    };

    public static final ExclusionStrategy EXCLUDE_FOR_BRIEF_TASK = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            return fieldAttributes.getAnnotation(ExcludeForJsonBriefTask.class) != null;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    };

    public static class ExcludeThisClass implements ExclusionStrategy {

        private Class<?> clazz;

        public ExcludeThisClass(Class<?> clazz) {
            this.clazz = clazz;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> aclazz) {
            return aclazz.getName().equals(clazz.getName());
        }
    }

    public static class ExcludeThisClasses implements ExclusionStrategy {

        private Class<?>[] clazzes;

        public ExcludeThisClasses(Class<?>... clazz) {
            this.clazzes = clazz;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> aclazz) {
            String className = aclazz.getName();
            for (Class clazz : this.clazzes) {
                if (!className.equals(clazz.getName())) {
                    continue;
                } else {
                    return true;
                }
            }
            return false;
        }
    }

    /*public static class ExcludeThisAnnotations implements ExclusionStrategy {

        private Class<?>[] annotations;

        public ExcludeThisAnnotations(Class<?>... annotations) {
            this.annotations = annotations;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            for (Class<> annotation : this.annotations) {
                if (fieldAttributes.getAnnotation(annotation) == null) {
                    continue;
                } else {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> aclazz) {
            return false;
        }
    }*/
}
