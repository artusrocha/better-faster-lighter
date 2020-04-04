package org.acme.entity;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

@ApplicationScoped
public class ActorRepository implements PanacheRepositoryBase<Actor, String> {


}
