package org.acme.repository.reactive;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.mysqlclient.MySQLClient;
import io.vertx.mutiny.mysqlclient.MySQLPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import org.acme.entity.category.Category;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class CategoryReactiveRepository {

    @Inject
    private MySQLPool client;

    private static final String SQL_DELETE = "DELETE FROM Category WHERE id = ?";
    private static final String SQL_FIND_ALL = "SELECT id, name FROM Category";
    private static final String SQL_FIND_BY_ID = "SELECT id, name FROM Category WHERE id = ?";
    private static final String SQL_CREATE = "INSERT INTO Category (name) VALUES (?)";
    private static final String SQL_COUNT_BY_ID = "SELECT count(1) AS count FROM Category WHERE id = ?";
    private static final String SQL_COUNT_BY_NAME = "SELECT count(1) AS count FROM Category WHERE name = ?";;

    public Boolean exists(Long id) {
        return existsUni(id).await().indefinitely();
    }

    public Uni<Boolean> existsUni(Long id) {
        return count(SQL_COUNT_BY_ID, Tuple.of(id)).onItem().apply(count -> count > 0L);
    }

    private Uni<Long> count(String sql, Tuple params) {
        return client.preparedQuery( sql, params)
                .onItem().apply(RowSet::iterator)
                .onItem().apply(row -> row.hasNext() ? row.next().getLong("count") : 0L);
    }

    public Boolean delete(Long attrId) {
        client.preparedQuery( SQL_DELETE, Tuple.of(attrId)).await().indefinitely();
        return !this.existsUni(attrId).await().indefinitely();
    }

    public List<Category> listAll() {
        return findAllMulti().collectItems().asList().await().indefinitely();
    }

    public Multi<Category> findAllMulti() {
        return client.query(SQL_FIND_ALL)
                .onItem()
                .produceMulti(set -> Multi.createFrom().items(() -> {
                    return StreamSupport.stream(set.spliterator(), false);
                }))
                .onItem().apply(this::assign); // Assign each row to a entity instance
    }

    private String placeholders(int size) {
        return String.join(",", Collections.nCopies(size, "?"));
    }

    public Optional<Category> findByIdOptional(Long id) {
        return findByIdUni(id).await().asOptional().indefinitely();
    }

    public Uni<Category> findByIdUni(Long id) {
        return client.preparedQuery( SQL_FIND_BY_ID, Tuple.of(id) )
                .onItem().apply(RowSet::iterator)
                .onItem().apply(iterator -> iterator.hasNext() ? assign(iterator.next()) : null);
    }

    public void persist(Category data) {
        create(data);
    }

    public Optional<Category> create(Category data) {
        Optional<Category> saved = createUni(data).await().asOptional().indefinitely();
        if(saved.isPresent()) {
            data.setId( saved.get().getId() );
        }
        return saved;
    }

    public Uni<Category> createUni(Category data) {
        return client.preparedQuery( SQL_CREATE, Tuple.of(data.getName()))
                .onItem()
                .apply( rows -> rows.property(MySQLClient.LAST_INSERTED_ID))
                .onItem()
                .apply( this::findByIdUni )
                .await().indefinitely();
    }

    private Category assign(Row row) {
        final Category cat = new Category();
        cat.setId(row.getLong("id"));
        cat.setName(row.getString("name"));
        return cat;
    }

    public Boolean existsByName(String name) {
        return existsByNameUni(name).await().indefinitely();
    }

    public Uni<Boolean> existsByNameUni(String name) {
        return count(SQL_COUNT_BY_NAME, Tuple.of(name)).onItem().apply(count -> count > 0L);
    }

}
