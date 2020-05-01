package org.acme.repository.reactive;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.mysqlclient.MySQLClient;
import io.vertx.mutiny.mysqlclient.MySQLPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;


abstract class ReactiveRepository<T, I> {
    
    @Inject
    private MySQLPool client;

    abstract protected String sqlToDelete();
    abstract protected String sqlToFindAll();
    abstract protected String sqlToFindById();
    abstract protected String sqlToCreate();
    abstract protected String sqlToCountById();
    abstract protected Tuple entityToTupleParameters(T data);
    abstract protected T assign(Row row) ;

    public Boolean exists(Long id) {
        return existsUni(id).await().indefinitely();
    }

    public Uni<Boolean> existsUni(Long id) {
        final String sql = sqlToCountById();
        return count(sql, Tuple.of(id)).onItem().apply(count -> count > 0L);
    }

    private Uni<Long> count(String sql, Tuple params) {
        return client.preparedQuery( sql, params)
                .onItem().apply(RowSet::iterator)
                .onItem().apply(row -> row.hasNext() ? row.next().getLong("count") : 0L);
    }

    public Boolean delete(Long attrId) {
        final String sql = sqlToDelete();
        client.preparedQuery( sql, Tuple.of(attrId)).await().indefinitely();
        return !this.existsUni(attrId).await().indefinitely();
    }

    public List<T> listAll() {
        return findAllMulti().collectItems().asList().await().indefinitely();
    }

    public Multi<T> findAllMulti() {
        final String sql =  sqlToFindAll();
        return client.query(sql)
                .onItem()
                .produceMulti(set -> Multi.createFrom().items(() -> {
                    return StreamSupport.stream(set.spliterator(), false);
                }))
                .onItem().apply(this::assign); // Assign each row to a entity instance
    }

    private String placeholders(int size) {
        return String.join(",", Collections.nCopies(size, "?"));
    }

    public Optional<T> findByIdOptional(I id) {
        return findByIdUni(id).await().asOptional().indefinitely();
    }

    public Uni<T> findByIdUni(I id) {
        final String sql =  sqlToFindById();
        return client.preparedQuery( sql, Tuple.of(id) )
                .onItem().apply(RowSet::iterator)
                .onItem().apply(iterator -> iterator.hasNext() ? assign(iterator.next()) : null);
    }

    public void persist(T data) {
        create(data);
    }

    public Optional<T> create(T data) {
        Optional<T> saved = createUni(data).await().asOptional().indefinitely();
        if(saved.isPresent()) {
            data = saved.get();
        }
        return saved;
    }

    public Uni<T> createUni(T data) {
        final String sql = sqlToCreate();
        final Tuple params = entityToTupleParameters(data);
        return client.preparedQuery( sql, params)
                .onItem()
                .apply( rows -> (I) rows.property(MySQLClient.LAST_INSERTED_ID))
                .onItem()
                .apply( this::findByIdUni )
                .await().indefinitely();
    }
}
