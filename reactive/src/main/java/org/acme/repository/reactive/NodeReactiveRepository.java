package org.acme.repository.reactive;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.mysqlclient.MySQLClient;
import io.vertx.mutiny.mysqlclient.MySQLPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import org.acme.entity.attribute.Attribute;
import org.acme.entity.node.Node;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class NodeReactiveRepository {

    @Inject
    private MySQLPool client;


    private static final String SQL_DELETE = "DELETE FROM Node WHERE id = ?";
    private static final String SQL_FIND_ALL = "SELECT id, name FROM Node";
    private static final String SQL_FIND_BY_ID = "SELECT id, name FROM Node WHERE id = ?";
    private static final String SQL_CREATE = "INSERT INTO Node (name) VALUES (?)";
    private static final String SQL_COUNT_BY_ID = "SELECT count(1) AS count FROM Node WHERE id = ?";
    private static final String SQL_COUNT_BY_NAME = "SELECT count(1) AS count FROM Node WHERE name = ?";;
    private static final String SQL_FIND_ATTRIBUTES_BY_NODE_ID = new StringBuilder()
        .append("SELECT a.id, a.value, c.id AS key_id, c.name AS key_name ")
        .append("FROM Attribute a INNER JOIN Category c ON a.key_id=c.id ")
        .append("WHERE a.id = ? ")
        .append("ORDER BY id ASC")
        .toString();

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

    public List<Node> listAll() {
        List<Node> nodes = findAllMulti().collectItems().asList().await().indefinitely();
        nodes.forEach(node -> node.setAttr( findAttrByNodeId( node.getId() ) ));
        return nodes;
    }

    public Multi<Node> findAllMulti() {
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

    public Optional<Node> findByIdOptional(Long id) {
        Optional<Node> nodeOpt = findByIdUni(id).await().asOptional().indefinitely();
        nodeOpt.ifPresent( node -> node.setAttr( findAttrByNodeId( node.getId() ) ));
        return nodeOpt;
    }

    public Uni<Node> findByIdUni(Long id) {
        return client.preparedQuery( SQL_FIND_BY_ID, Tuple.of(id) )
                .onItem().apply(RowSet::iterator)
                .onItem().apply(iterator -> iterator.hasNext() ? assign(iterator.next()) : null);
    }

    public void persist(Node data) {
        create(data);
    }

    public Optional<Node> create(Node data) {
        Optional<Node> saved = createUni(data).await().asOptional().indefinitely();
        saved.ifPresent(node -> data.setId( node.getId() ) );
        return saved;
    }

    public Uni<Node> createUni(Node data) {
        return client.preparedQuery( SQL_CREATE, Tuple.of(data.getName()))
                .onItem()
                .apply( rows -> rows.property(MySQLClient.LAST_INSERTED_ID))
                .onItem()
                .apply( this::findByIdUni )
                .await().indefinitely();
    }

    private Node assign(Row row) {
        final Node node = new Node();
        node.setId(row.getLong("id"));
        node.setName(row.getString("name"));
        node.setDescription(row.getString("description"));
        node.setShortname(row.getString("shortname"));
        return node;
    }

    public Boolean existsByName(String name) {
        return existsByNameUni(name).await().indefinitely();
    }

    public Uni<Boolean> existsByNameUni(String name) {
        return count(SQL_COUNT_BY_NAME, Tuple.of(name)).onItem().apply(count -> count > 0L);
    }

    public List<Attribute> findAttrByNodeId(Long id) {
        return StreamSupport.stream(
                    client.preparedQueryAndAwait( SQL_FIND_ATTRIBUTES_BY_NODE_ID, Tuple.of(id) )
                            .spliterator(),false)
                .map( AttributeReactiveRepository::from )
                .collect(Collectors.toList());
    }


}
