package game.role.player.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import db.cache.EntityCacheService;
import game.role.player.entity.PlayerEnt;
import game.role.player.model.Player;
import game.role.player.resource.PlayerResource;
import resource.anno.Static;

/**
 * @author : ddv
 * @since : 2019/5/15 上午10:05
 */
@Component
public class PlayerManager {

    @Autowired
    private EntityCacheService<String, PlayerEnt> entEntityCache;

    @Static
    private Map<Integer, PlayerResource> resourceMap;

    public PlayerEnt loadOrCreate(String accountId) {
        return entEntityCache.loadOrCreate(PlayerEnt.class, accountId, PlayerEnt::valueOf);
    }

    public PlayerEnt load(String accountId) {
        return entEntityCache.load(PlayerEnt.class, accountId);
    }

    public void saveEntity(PlayerEnt playerEnt) {
        entEntityCache.save(playerEnt);
    }

    public PlayerResource getPlayerResource(Integer id) {
        return resourceMap.get(id);
    }

    public Player loadPlayerById(long playerId) {
        return entEntityCache.getEntityCacheMap().get(PlayerEnt.class).getCache().values().stream()
            .filter(playerEnt -> {
                return playerEnt.getPlayer().getPlayerId() == playerId;
            }).findAny().get().getPlayer();
    }
}
