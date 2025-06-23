package com.ratan.loginapi.actor.service;

import com.ratan.loginapi.actor.dto.GenericSearchRequest;
import com.ratan.loginapi.actor.entity.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ActorService {
    List<Actor> getAllActors();

    Optional<Actor> getActorById(Integer id);

    Actor createActor(Actor actor);

    Actor updateActor(Integer id, Actor updatedActor);

    void deleteActor(Integer id);

    Page<Actor> getActorsWithPagination(Pageable pageable);

    Page<Actor> dynamicSearch(GenericSearchRequest request, Pageable pageable);
}
