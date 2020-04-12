package org.acme.entity.node;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.nio.file.OpenOption;
import java.util.Optional;

@ApplicationScoped
public class NodeRepository implements PanacheRepositoryBase<Node, Long> {


    public Boolean exists(Long id) {
        return this.count("shortname=?1", id) > 0 ;
    }

    public long delete(Long id) {
        return this.delete("shortname=?1", id);
    }

    public Optional<Node> create(Node node) {
        this.persist(node);
        return this.isPersistent( node ) ? Optional.of(node) :
                                            Optional.empty();
    }

}
