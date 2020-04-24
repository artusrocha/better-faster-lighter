package org.acme.entity.category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepo {

    Optional<Category> findByIdOptional(Long keyId);

    List<Category> listAll();

    Boolean existsByName(String name);

    Optional<Category> create(Category category);

    Boolean delete(Long categoryId);

    Boolean exists(Long categoryId);
}
