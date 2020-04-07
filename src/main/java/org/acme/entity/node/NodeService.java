package org.acme.entity.node;

import org.acme.entity.attribute.Attribute;
import org.acme.entity.category.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class NodeService {

	@Inject
	private NodeRepository repo;

	public List<Node> getAll() {
		return repo.listAll();
	}

	@Transactional
	public List<Node> populate() {
		List<Attribute> southAmerAttr = new ArrayList<>();
		Category continent = new Category("Continent");
		continent.persist();
		Category language = new Category("Language");
		language.persist();
		southAmerAttr.add( new Attribute(continent, "South America") );
		southAmerAttr.add( new Attribute(language, "Spanish") );
		southAmerAttr.stream().forEach( each -> each.persist() );
		
		List<Node> Nodes = new ArrayList<>();
		Nodes.add(new Node("CO", "Colombia", southAmerAttr));
		Nodes.add(new Node("AR", "Argentina", southAmerAttr));
		repo.persist(Nodes);
		return Nodes;
	}

	@Transactional
	public Optional<Node> create(Node node) {
		repo.persistAndFlush(node);
		return repo.isPersistent( node ) ? Optional.of(node) :
										   Optional.empty();
	}

	public Optional<Node> findById(Long id) {
		return repo.findByIdOptional(id);
	}

	@Transactional
	public Boolean delete(Long id) {
		repo.deleteById(id);
		return !repo.existsById(id);
	}
}