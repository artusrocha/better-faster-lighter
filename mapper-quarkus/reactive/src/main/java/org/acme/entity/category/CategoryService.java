package org.acme.entity.category;

import org.acme.repository.reactive.CategoryReactiveRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class CategoryService {

    @Inject
    private CategoryRepository repo;

    private CategoryRepository getRepo() {
        return repo;
    }

    public Optional<Category> findById(Long keyId) {
        return getRepo().findByIdOptional(keyId);
    }

    public List<Category> getAll() {
        return getRepo().listAll();
    }

    @Transactional
    public Optional<Category> create(Category category) {
        if ( ! getRepo().existsByName( category.getName() ) ) {
            try{
                return getRepo().create(category);
            }
            catch(Exception e) { e.printStackTrace(); }
        }
        return Optional.empty();
    }

    @Transactional
    public Boolean delete(Long categoryId) {
        try {
            getRepo().delete(categoryId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return !getRepo().exists(categoryId);
    }
}
