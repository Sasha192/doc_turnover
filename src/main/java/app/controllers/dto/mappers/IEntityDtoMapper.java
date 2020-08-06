package app.controllers.dto.mappers;

public interface IEntityDtoMapper<T, K> {
    // T - entity, K - Dto
    K getDto(T entity);

    T getEntity(K dto);
}
