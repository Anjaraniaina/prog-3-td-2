package app.foot.service;

import app.foot.repository.TeamRepository;
import app.foot.repository.entity.TeamEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TeamService {
    private final TeamRepository repository;

    public TeamEntity findById(Integer id){
        Optional<TeamEntity> team = repository.findById(id);
        if (team.isPresent()) {
            return team.get();
        } else {
            throw new EntityNotFoundException("Team with id " + id + " not found");
        }
    }
}
