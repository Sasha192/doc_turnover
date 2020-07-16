package app.controllers.dto;

public interface IEntityDtoMapper<T, K> {
    // T - entity, K - Dto
    K getDto(T entity);

    T getEntity(K dto);
}
