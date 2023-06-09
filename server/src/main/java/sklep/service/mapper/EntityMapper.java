package sklep.service.mapper;

import java.util.List;
import java.util.Set;

public interface EntityMapper<E, D> {
    E toEntity(D dto);
    D toDto(E dto);

    List<E> toEntity(List<D> dtoList);
    List<D> toDto(List<E> entityList);

    Set<E> toEntity(Set<D> dtoSet);
    Set<D> toDto(Set<E> entitySet);
}
