package org.acme.entity.category;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class CategoryService {

    @Inject
    private CategoryRepository repo;

    public Optional<Category> findById(Long keyId) {
        return repo.findByIdOptional(keyId);
    }

    public List<Category> getAll() {
        return repo.findAll().list();
    }

    @Transactional
    public Optional<Category> create(Category category) {
        if ( ! repo.existsByName( category.getName() ) ) {
            try{
                repo.persist(category);
                if( repo.isPersistent(category) )
                    return Optional.ofNullable(category);
            }
            catch(Exception e) { e.printStackTrace(); }
        }
        return Optional.empty();
    }

    @Transactional
    public Boolean delete(Long categoryId) {
        try {
            repo.delete(categoryId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return !repo.exists(categoryId);
    }
}
