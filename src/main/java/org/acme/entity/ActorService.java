package org.acme.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class ActorService {

	@Inject
	private ActorRepository repo;

	public List<Actor> getAll() {
		return repo.listAll();
	}

	@Transactional
	public List<Actor> populate() {
		List<Attribute> southAmerAttr = new ArrayList<>();
		Category continent = new Category("Continent");
		continent.persist();
		Category language = new Category("Language");
		language.persist();
		southAmerAttr.add( new Attribute(continent.getName(), "South America") );
		southAmerAttr.add( new Attribute(language.getName(), "Spanish") );
		southAmerAttr.stream().forEach( each -> each.persist() );
		
		List<Actor> actors = new ArrayList<>();
		actors.add(new Actor("CO", "Colombia", southAmerAttr));
		actors.add(new Actor("AR", "Argentina", southAmerAttr));
		repo.persist(actors);
		return actors;
	}

	@Transactional
	public Optional<Actor> create(Actor actor) {
		if ( ! repo.existsById( actor.getShortname() ) ) {
			repo.persistAndFlush(actor);
			return repo.findByIdOptional( actor.getShortname() );
		}
		return Optional.empty();
	}

	public Optional<Actor> findById(String id) {
		return repo.findByIdOptional(id);
	}

	@Transactional
	public Boolean delete(String id) {
		repo.deleteById(id);
		return !repo.existsById(id);
	}
}