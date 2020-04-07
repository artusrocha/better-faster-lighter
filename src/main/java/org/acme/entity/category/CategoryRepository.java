package org.acme.entity.category;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class CategoryRepository implements PanacheRepositoryBase<Category, Long> {

    public Boolean existsByName(String name) {
        return this.count("name",name) > 0;
    }

    public Boolean delete(Long categoryId) {
        this.delete("id=:id", categoryId);
        return !this.exists(categoryId);
    }

    public Boolean exists(Long categoryId) {
        return this.count("id", categoryId) > 0;
    }
}
