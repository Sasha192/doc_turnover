package app.models.basic;

@Deprecated
/**
 * BETTER USE SOME OBSERVER PATTERN
 */
public interface IChanger<T, K> {

    void change(T entity, K newProperty);

}
