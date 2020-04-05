package org.acme.entity;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class AttributeRepository implements PanacheRepositoryBase<Attribute, String> {


    public Boolean exists(String category, String value) {
        return this.count("key_name=?1 and value=?2", category, value) > 0 ;
    }

    public Long deleteByKeyValue(String category, String value) {
        return this.delete("key_name=?1 and value=?2", category, value);
    }

    public Long deleteByGuid(UUID guid) {
        return this.delete("guid=?1", guid);
    }

    public Boolean exists(UUID guid) {
        return this.count("guid=?1", guid.toString()) > 0 ;
    }

    public Optional<Attribute> findByGuid(UUID guid) {
        System.out.println("guid: "+guid.toString());
        System.out.println("value: "+this.find("guid=?1",
                guid
        ).firstResult().getValue() );
        return Optional.ofNullable( this.find("guid=?1",
                guid
        ).firstResult() );
                //.firstResultOptional() ;
    }
}
