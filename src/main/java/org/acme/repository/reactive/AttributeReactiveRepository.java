package org.acme.repository.reactive;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import io.vertx.mutiny.mysqlclient.MySQLPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import org.acme.entity.attribute.Attribute;
import org.acme.entity.category.Category;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class AttributeReactiveRepository {
    
    @Inject
    private MySQLPool client;

    public Boolean exists(Category category, String value) {
        return Boolean.FALSE; //this.count("key=?1 and value=?2", category, value) > 0 ;
    }

    public Boolean exists(Long id) {
        return Boolean.FALSE; //this.count("id=?1", id) > 0 ;
    }

    public Optional<Attribute> findByValue(String v) {
        return reactFindByValue(v).await().asOptional().indefinitely();
    }

    public Uni<Attribute> reactFindByValue(String v) {
        return client.preparedQuery("SELECT id, value FROM Attribute WHERE value = $1", Tuple.of(v))
                .onItem().apply(RowSet::iterator)
                .onItem().apply(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
    }

    public Boolean delete(Long attrId) {
        return !this.exists(attrId);
    }

    public List<Attribute> listAll() {
        return reactFindAll().collectItems().asList().await().indefinitely();
    }

    public Multi<Attribute> reactFindAll() {
        return client.query("SELECT id, value FROM Attribute ORDER BY value ASC")
                // Create a Multi from the set of rows:
                .onItem()
                .produceMulti(set -> Multi.createFrom().items(() -> {
                    return StreamSupport.stream(set.spliterator(), false);
                }))
                .onItem().apply(this::from); // For each row create a attr instance
    }

    public Optional<Attribute> findByIdOptional(Long id) {
        return reactFindById(id).await().asOptional().indefinitely();// .map( attr -> Optional.of(attr) );
    }

    public Uni<Attribute> reactFindById(Long id) {
        return client.preparedQuery("SELECT id, value FROM Attribute WHERE id = ?", Tuple.of(id))
                .onItem().apply(RowSet::iterator)
                .onItem().apply(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
    }

    private Attribute from(Row next) {
        return new Attribute();
    }

    public void persist(Attribute attribute) {
    }
}
