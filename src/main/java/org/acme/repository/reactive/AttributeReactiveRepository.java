package org.acme.repository.reactive;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import io.vertx.mutiny.mysqlclient.MySQLClient;
import io.vertx.mutiny.mysqlclient.MySQLPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import org.acme.entity.attribute.Attribute;
import org.acme.entity.category.Category;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.*;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class AttributeReactiveRepository {
    
    @Inject
    private MySQLPool client;

    public Boolean exists(Category category, String value) {
        return existsUni(category, value).await().indefinitely();
    }

    public Boolean exists(Long id) {
        return existsUni(id).await().indefinitely();
    }

    public Uni<Boolean> existsUni(Category category, String value) {
        final String sql = "SELECT count(1) as count FROM Attribute WHERE key_id = ? AND value = ?";
        return count(sql, Tuple.of(category.getId(), value)).onItem().apply(count -> count > 0L);
    }

    public Uni<Boolean> existsUni(Long id) {
        final String sql = "SELECT count(1) as count FROM Attribute WHERE id = ?";
        return count(sql, Tuple.of(id)).onItem().apply(count -> count > 0L);
    }

    private Uni<Long> count(String sql, Tuple params) {
        return client.preparedQuery( sql, params)
                .onItem().apply(RowSet::iterator)
                .onItem().apply(row -> row.hasNext() ? row.next().getLong("count") : 0L);
    }

    public Optional<Attribute> findByValue(String v) {
        return findByValueUni(v).await().asOptional().indefinitely();
    }

    public Uni<Attribute> findByValueUni(String v) {
    	final String sql = "SELECT id, value FROM Attribute WHERE value = ?";
        return client.preparedQuery( sql, Tuple.of(v))
                .onItem().apply(RowSet::iterator)
                .onItem().apply(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
    }

    public Boolean delete(Long attrId) {
        final String sql = "DELETE FROM Attribute WHERE id = ?";
        client.preparedQuery( sql, Tuple.of(attrId)).await().indefinitely();
        return !this.existsUni(attrId).await().indefinitely();
    }

    public List<Attribute> listAll() {
        return findAllMulti().collectItems().asList().await().indefinitely();
    }

    public Multi<Attribute> findAllMulti() {
        final String sql = new StringBuilder()
                .append("SELECT a.id, a.value, c.id AS key_id, c.name AS key_name ")
                .append("FROM Attribute a ")
                .append("INNER JOIN Category c ON a.key_id=c.id ")
                .append("ORDER BY value ASC")
                .toString();
        return client.query(sql)
                .onItem()
                .produceMulti(set -> Multi.createFrom().items(() -> {
                    return StreamSupport.stream(set.spliterator(), false);
                }))
                .onItem().apply(this::from); // For each row create a attr instance
    }

    public Multi<Attribute> findAllInMulti(Collection<Long> ids) {
        final String sql = new StringBuilder()
                .append("SELECT a.id, a.value, c.id AS key_id, c.name AS key_name ")
                .append("FROM Attribute a ")
                .append("INNER JOIN Category c ON a.key_id=c.id ")
                .append("WHERE a.id IN (").append( placeholders(ids.size()) ).append(')')
                .toString();
        Tuple ids_params = Tuple.tuple();
        ids.forEach( id -> ids_params.addLong(id) );
        return client.preparedQuery( sql, ids_params )
                .onItem().produceMulti(set -> Multi.createFrom().items(() -> {
                    return StreamSupport.stream(set.spliterator(), false);
                })).onItem().apply(this::from); // For each row create a attr instance
    }

    private String placeholders(int size) {
        return String.join(",", Collections.nCopies(size, "?"));
    }

    public Optional<Attribute> findByIdOptional(Long id) {
        return reactFindById(id).await().asOptional().indefinitely();// .map( attr -> Optional.of(attr) );
    }

    public Uni<Attribute> reactFindById(Long id) {
        System.out.println(">>>>>>>>>>> "+id);
        final String sql = new StringBuilder()
                .append("SELECT a.id, a.value, c.id AS key_id, c.name AS key_name ")
                .append("FROM Attribute a ")
                .append("INNER JOIN Category c ON a.key_id=c.id ")
                .append("WHERE a.id = ?")
                .toString();
        return client.preparedQuery( sql, Tuple.of(id) )
                .onItem().apply(RowSet::iterator)
                .onItem().apply(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
    }

    private Attribute from(Row row) {
        final Attribute obj = new Attribute();
        obj.setId(row.getLong("id"));
        obj.setValue(row.getString("value"));
        obj.setKey( rowToCategory(row) );
        System.out.println( obj );
        return obj;
    }

    private Category rowToCategory(Row row) {
        final Category cat = new Category();
        cat.setId(row.getLong("key_id"));
        cat.setName(row.getString("key_name"));
        return cat;
    }

    public void persist(Attribute attribute) {
        create(attribute);
    }

    public Optional<Attribute> create(Attribute attribute) {
        Optional<Attribute> saved = createUni(attribute).await().asOptional().indefinitely();
        if(saved.isPresent())
           attribute = saved.get();
        return saved;
    }

    public Uni<Attribute> createUni(Attribute attribute) {
        final String sql = "INSERT INTO Attribute (value, key_id) VALUES (?, ?)";
        final Tuple params = Tuple.of(attribute.getValue(), attribute.getKey().getId());
        return client.preparedQuery( sql, params)
                .onItem()
                .apply( rows -> rows.property(MySQLClient.LAST_INSERTED_ID))
                .onItem()
                .apply( this::reactFindById )
                .await().indefinitely();
    }
}
