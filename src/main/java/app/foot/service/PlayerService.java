package app.foot.service;

import app.foot.model.Player;
import app.foot.repository.PlayerRepository;
import app.foot.repository.entity.PlayerEntity;
import app.foot.repository.mapper.PlayerMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlayerService {
    private final PlayerRepository repository;
    private final PlayerMapper mapper;

    public PlayerEntity findById(Integer id){
        Optional<PlayerEntity> player = repository.findById(id);
        if (player.isPresent()) {
            return player.get();
        } else {
            throw new EntityNotFoundException("Match with id " + id + " not found");
        }
    }
    public List<Player> getPlayers() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    public List<Player> createPlayers(List<Player> toCreate) {
        return repository.saveAll(toCreate.stream()
                        .map(mapper::toEntity)
                        .toList()).stream()
                .map(mapper::toDomain)
                .toList();
    }

    public List<Player> updatePlayers(List<Player> toUpdate) {
        return repository.saveAll(toUpdate.stream()
                        .map(mapper::toEntity)
                        .toList()).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
