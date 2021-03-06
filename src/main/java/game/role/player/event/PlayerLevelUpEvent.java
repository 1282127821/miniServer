package game.role.player.event;

import ebus.model.IEvent;
import game.role.player.model.Player;

/**
 * @author : ddv
 * @since : 2019/6/25 上午11:48
 */

public class PlayerLevelUpEvent implements IEvent {

    private Player player;

    public static PlayerLevelUpEvent valueOf(Player player) {
        PlayerLevelUpEvent event = new PlayerLevelUpEvent();
        event.player = player;
        return event;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "PlayerLevelUpEvent{" + "player=" + player + '}';
    }
}
