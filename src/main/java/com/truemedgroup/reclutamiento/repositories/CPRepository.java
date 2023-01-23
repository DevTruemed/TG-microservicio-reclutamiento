package com.truemedgroup.reclutamiento.repositories;

import com.truemedgroup.reclutamiento.models.address.CodigoPostal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CPRepository extends CrudRepository<CodigoPostal, Integer> {

    @Query(nativeQuery = true, value = "SELECT TOP 5 * FROM CodigosPostales WHERE CP LIKE CONCAT('%',?1,'%')")
    List<CodigoPostal> getCP(Integer cp);

}
