package game.base.item.base.model;

import game.base.message.exception.RequestException;
import game.role.player.model.Player;

/**
 * @author : ddv
 * @since : 2019/6/26 下午12:27
 */

public class ExchangeCard extends AbstractUsableItem {

    @Override
    public void verifyCanUseThrow(Player player, int num) throws RequestException {

    }

    @Override
    public boolean verifyCanUse(Player player, int num) {
        return player.getLevel() > 10;
    }

    @Override
    public void useEffect(Player player, int num) {

    }
}
