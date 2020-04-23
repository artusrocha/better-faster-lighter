package org.acme.entity.node;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.acme.entity.attribute.Attribute;
import org.acme.entity.category.Category;
import org.acme.repository.reactive.NodeReactiveRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class NodeReactiveService {

	@Inject
	private NodeReactiveRepository repo;

//        @ConfigProperty(name = "reactive.repository.enabled")
//        private Optional<Boolean> reactiveRepo;
//
//        private NodeRepo getRepo() {
//            return reactiveRepo.orElse(Boolean.FALSE) ? reactRepo : repo;
//        }

	public Multi<Node> getAll() {
		return repo.findAllMulti();
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
		
		List<Node> nodes = new ArrayList<>();
		nodes.add(new Node("CO", "Colombia", southAmerAttr));
		nodes.add(new Node("AR", "Argentina", southAmerAttr));
		return nodes.stream()
				.map(node -> repo.create(node))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList());
	}

	@Transactional
	public Optional<Node> create(Node node) {
		return repo.create(node);
	}

	public Uni<Node> findById(Long id) {
		return repo.findByIdUni(id);
	}

	@Transactional
	public Boolean delete(Long id) {
		repo.delete(id);
		return !repo.exists(id);
	}
}
