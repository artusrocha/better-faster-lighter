package org.acme.entity;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

@ApplicationScoped
public class ActorRepository implements PanacheRepositoryBase<Actor, String> {


    public Boolean existsById(String id) {
        return this.count("shortname=?1", id) > 0 ;
    }

    public long deleteById(String id) {
        return this.delete("shortname=?1", id);
    }
}
