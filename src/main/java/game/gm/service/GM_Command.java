package game.gm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import game.base.game.attribute.AttributeContainer;
import game.base.game.attribute.id.AttributeIdEnum;
import game.base.game.attribute.model.PlayerAttributeContainer;
import game.scene.map.service.SceneMapManager;
import game.user.equip.model.EquipStorage;
import game.user.equip.packet.SM_EquipStorage;
import game.user.equip.service.EquipService;
import game.user.item.base.model.AbstractItem;
import game.user.login.entity.UserEnt;
import game.user.pack.model.Pack;
import game.user.pack.service.PackService;
import game.user.player.model.Player;
import game.user.player.service.PlayerService;
import net.model.USession;
import net.utils.PacketUtil;
import spring.SpringContext;
import utils.SimpleUtil;

/**
 * gm命令后台实现
 *
 * @author : ddv
 * @since : 2019/5/7 下午3:01
 */

@Component
public class GM_Command {

    private static final Logger logger = LoggerFactory.getLogger(GM_Command.class);

    @Autowired
    private SceneMapManager sceneMapManager;
    @Autowired
    private PackService packService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private EquipService equipService;

    public void logUserEnt(USession session) {
        UserEnt userEnt = SpringContext.getLoginService().getUserEnt(session);
        logger.info(userEnt.toString());
    }

    public void logScene(USession session) {
        logger.info("服务器当前地图[{}]张", sceneMapManager.getSceneMaps().size());

        sceneMapManager.getPlayerMaps().forEach((playerId, id) -> {
            logger.info("玩家[{}],存在于地图[{}]", playerId, id);
        });
    }

    public void logMap(USession session, long mapId) {
        SpringContext.getSceneMapService().logBasicMapInfo(mapId);
    }

    public void logPlayer(USession session) {
        Player player =
            SpringContext.getPlayerService().getPlayerByAccountId(SimpleUtil.getAccountIdFromSession(session));
        AttributeContainer attributeContainer = player.getAttributeContainer();
        Pack pack = SpringContext.getPackService().getPlayerPack(player);
        logger.info(player.toString());
    }

    public void run(USession session) {
        Player player = SimpleUtil.getPlayerFromSession(session);
        PlayerAttributeContainer attributeContainer = player.getAttributeContainer();
        attributeContainer.putAttributes(AttributeIdEnum.BASE, playerService.getResource(1).getAttributeList(), null);
        System.out.println(1);

    }

    public void addItem(USession session, Long itemConfigId, int num) {
        Player player = SimpleUtil.getPlayerFromSession(session);
        AbstractItem item = packService.createItem(itemConfigId);
        packService.addItem(player, item, num);
    }

    public void reduceItem(USession session, Long itemConfigId, int num) {
        Player player = SimpleUtil.getPlayerFromSession(session);
        AbstractItem item = packService.createItem(itemConfigId);
        packService.reduceItem(player, item, num);
    }

    public void equip(USession session, Long itemConfigId, int position) {
        Player player = SimpleUtil.getPlayerFromSession(session);
        equipService.equip(player, itemConfigId, position);
    }

    public void logEquipStorage(USession session) {
        Player player = SimpleUtil.getPlayerFromSession(session);
        EquipStorage equipStorage = equipService.getEquipStorage(player);
        PacketUtil.send(player, SM_EquipStorage.valueOf(equipStorage));
    }

    public void enhanceEquipSquare(USession session, int position) {
        Player player = SimpleUtil.getPlayerFromSession(session);
        equipService.enhance(player, position);
    }

}
