package org.acme.entity;

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
	private AttributeRepository repo;

	public List<Attribute> getAll() {
		return repo.listAll();
	}

	@Transactional
	public Optional<Attribute> create(Attribute attribute) {
		if ( ! repo.exists( attribute.getKeyName(), attribute.getValue() ) ) {
			try{
				repo.persist( attribute );
				return Optional.ofNullable(attribute);
			}
			catch(Exception e) { e.printStackTrace(); }
		}
		return Optional.empty();
	}

	public Optional<Attribute> findById(String id) {
		return repo.findByIdOptional(id);
	}

	@Transactional
	public Boolean delete(String key, String value) {
		try {
			repo.deleteByKeyValue(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return !repo.exists(key, value);
	}

	@Transactional
	public Boolean delete(UUID guid) {
		try {
			repo.deleteByGuid(guid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return !repo.exists(guid);
	}

	public Optional<Attribute> findByGuid(UUID guid) {
		return repo.findByGuid(guid);
	}
}