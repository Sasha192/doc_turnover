package app.models;

@Deprecated
/**
 * BETTER USE SOME OBSERVER PATTERN
 */
public interface IChanger<T, K> {

    void change(T entity, K newProperty);

}
