package com.ratan.loginapi.actor.service;

import com.ratan.loginapi.actor.dto.GenericSearchRequest;
import com.ratan.loginapi.actor.dto.SpecificationBuilder;
import com.ratan.loginapi.actor.entity.Actor;
import com.ratan.loginapi.actor.repo.ActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActorServiceImpl implements ActorService {
    private final ActorRepository actorRepository;

    @Override
    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }

    @Override
    public Optional<Actor> getActorById(Integer id) {
        return actorRepository.findById(id);
    }

    @Override
    public Actor createActor(Actor actor) {
        return actorRepository.save(actor);
    }

    @Override
    public Actor updateActor(Integer id, Actor updatedActor) {
        return actorRepository.findById(id)
                .map(actor -> {
                    actor.setFirstName(updatedActor.getFirstName());
                    actor.setLastName(updatedActor.getLastName());
                    actor.setLastUpdate(updatedActor.getLastUpdate());
                    return actorRepository.save(actor);
                })
                .orElseThrow(() -> new RuntimeException("Actor not found with ID: " + id));
    }

    @Override
    public void deleteActor(Integer id) {
        actorRepository.deleteById(id);
    }

    @Override
    public Page<Actor> getActorsWithPagination(Pageable pageable) {
        return actorRepository.findAll(pageable);
    }

    @Override
    public Page<Actor> dynamicSearch(GenericSearchRequest request, Pageable pageable) {
        SpecificationBuilder<Actor> builder = new SpecificationBuilder<>();
        Specification<Actor> spec = builder.buildFromFilters(request.getFilters());
        return actorRepository.findAll(spec, pageable);
    }
}
