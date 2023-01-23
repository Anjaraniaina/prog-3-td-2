package app.foot.service;

import app.foot.repository.PlayerEntityRepository;
import app.foot.repository.entity.PlayerEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class PlayerService {

    private final PlayerEntityRepository repository;

    public PlayerEntity findById(Integer id){
        return repository.getById(id);
    }

    public PlayerEntity findByName(String name){
        return repository.findAll().stream()
                .filter((PlayerEntity playerEntity) -> Objects.equals(playerEntity.getName(), name))
                .toList().get(0);
    }
}
