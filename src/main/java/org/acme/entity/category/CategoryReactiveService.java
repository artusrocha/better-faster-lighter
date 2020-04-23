package org.acme.entity.category;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.acme.FF4JConfig;
import org.acme.repository.reactive.CategoryReactiveRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;


@ApplicationScoped
public class CategoryReactiveService {

    @Inject
    private CategoryReactiveRepository repo;

    @Inject
    private FF4JConfig ffConfig;

    private CategoryReactiveRepository getRepo() {
        return repo;
    }

    public Uni<Category> findById(Long keyId) {
        return getRepo().findByIdUni(keyId);
    }

    public Multi<Category> getAll() {
        return getRepo().findAllMulti();
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
