package org.acme.entity.attribute;

import org.acme.entity.category.Category;
import org.acme.entity.category.CategoryService;
import org.acme.repository.reactive.AttributeReactiveRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@ApplicationScoped
public class AttributeService {

	@Inject
	private AttributeReactiveRepository repo;

	public List<Attribute> getAll() {
		return repo.listAll();
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

	public Optional<Attribute> findById(Long id) {
		return repo.findByIdOptional (id);
	}

	public Optional<Attribute> findByValue(String v) {
		return repo.findByValue(v);
	}
}