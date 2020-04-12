package org.acme.entity.attribute;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.hibernate.orm.panache.runtime.JpaOperations;
import org.acme.entity.category.Category;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class AttributeRepository implements PanacheRepositoryBase<Attribute, Long> {

    @Inject
    EntityManager em;

    public Boolean exists(Category category, String value) {
        return this.count("key=?1 and value=?2", category, value) > 0 ;
    }

    public Boolean exists(Long id) {
        return this.count("id=?1", id) > 0 ;
    }

    public Optional<Attribute> findByValue(String v) {
        System.out.println("Value: "+v);
        return this.find("value",v).firstResultOptional() ;
    }

    public Boolean delete(Long attrId) {
        this.delete("id=:id", attrId);
        return !this.exists(attrId);
    }
}
