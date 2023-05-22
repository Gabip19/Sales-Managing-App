package salesapp.repository;

import salesapp.domain.Entity;

import java.io.Serializable;

public interface Repository<ID extends Serializable, E extends Entity<ID>> {
    void add(E elem);
    void delete(ID id);
    void update(E elem, ID id);
    E findById(ID id);
    Iterable<E> findAll();
}
