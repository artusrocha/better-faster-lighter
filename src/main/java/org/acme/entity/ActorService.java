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
		southAmerAttr.add( new Attribute(continent, "South America") );
		southAmerAttr.add( new Attribute(language, "Spanish") );
		southAmerAttr.stream().forEach( each -> { each.persist(); });
		
		List<Actor> actors = new ArrayList<>();
		actors.add(new Actor("CO", "Colombia", southAmerAttr));
		actors.add(new Actor("AR", "Argentina", southAmerAttr));
		Actor.persist(actors);
		return actors;
	}

	@Transactional
	public Optional<Actor> create(Actor actor) {
		repo.persistAndFlush(actor);
		if(actor.isPersistent())
			Optional.of( actor);
		return Optional.empty();
			
	}

	public Optional<Actor> findByShortname(String shortname) {
		return Optional.ofNullable( repo.findById(shortname));
	}
}