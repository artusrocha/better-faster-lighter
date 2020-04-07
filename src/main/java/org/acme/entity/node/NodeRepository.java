package org.acme.entity.node;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

@ApplicationScoped
public class NodeRepository implements PanacheRepositoryBase<Node, Long> {


    public Boolean existsById(Long id) {
        return this.count("shortname=?1", id) > 0 ;
    }

    public long deleteById(Long id) {
        return this.delete("shortname=?1", id);
    }
}
