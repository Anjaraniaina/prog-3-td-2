package app.foot.controller.rest.mapper;

import app.foot.controller.rest.Player;
import app.foot.repository.PlayerRepository;
import app.foot.service.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PlayerRestMapper {

    public final PlayerRepository playerRepository;
    public final PlayerService playerService;
    public Player toRest(app.foot.model.Player domain) {
        return Player.builder()
                .id(domain.getId())
                .name(domain.getName())
                .isGuardian(domain.getIsGuardian())
                .teamName(domain.getTeamName())
                .build();
    }

    public app.foot.model.Player toDomain(Player rest) {
        return app.foot.model.Player.builder()
                .id(rest.getId())
                .isGuardian(rest.getIsGuardian())
                .name(rest.getName())
                .teamName(rest.getTeamName())
                .build();
    }

    public app.foot.model.Player toDomainUpdate(Player rest) {
        return app.foot.model.Player.builder()
                .id(rest.getId())
                .isGuardian(rest.getIsGuardian())
                .name(rest.getName())
                .teamName(playerService.findById(rest.getId()).getTeam().getName())
                .build();
    }
}
