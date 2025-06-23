package com.ratan.loginapi.actor.repo;

import com.ratan.loginapi.actor.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ActorRepository extends JpaRepository<Actor,Integer>, JpaSpecificationExecutor<Actor> {
    List<Actor> findByLastName(String lastName);

    List<Actor> findByLastNameAndFirstNameIgnoreCaseOrderByLastUpdate(String lastName, String firstName);
}
