package org.acme.entity.attribute;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.acme.repository.reactive.AttributeReactiveRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class AttributeReactiveService {

	@Inject
	private AttributeReactiveRepository repo;

	public Multi<Attribute> getAll() {
		return repo.findAllMulti();
	}

	@Transactional
	public Optional<Attribute> create(Attribute attribute) {
		if ( ! repo.exists( attribute.getKey(), attribute.getValue() ) ) {
			try{
				repo.persist( attribute );
				return Optional.ofNullable(attribute);
			}
			catch(Exception e) { e.printStackTrace(); }
		}
		return Optional.empty();
	}

	@Transactional
	public Boolean delete(Long id) {
		try {
			repo.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return !repo.exists(id);
	}

	public Uni<Attribute> findById(Long id) {
		return repo.findByIdUni (id);
	}

	public Uni<Attribute> findByValue(String v) {
		return repo.findByValueUni(v);
	}
}