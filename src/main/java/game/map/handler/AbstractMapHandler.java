package game.map.handler;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.MessageEnum;
import game.base.executor.util.ExecutorUtils;
import game.base.fight.model.pvpunit.BaseCreatureUnit;
import game.base.fight.model.skill.action.handler.BaseActionHandler;
import game.base.message.I18N;
import game.base.message.exception.RequestException;
import game.base.skill.constant.SkillEnum;
import game.map.constant.MapGroupType;
import game.map.model.Grid;
import game.role.player.model.Player;
import game.world.base.command.scene.EnterMapCommand;
import game.world.base.resource.MiniMapResource;
import game.world.base.service.WorldManager;

/**
 * 地图处理器
 *
 * @author : ddv
 * @since : 2019/7/2 下午9:22
 */

public abstract class AbstractMapHandler implements IMapHandler, IMovableMapHandler {

    private static final Logger logger = LoggerFactory.getLogger(AbstractMapHandler.class);

    private static final Map<Integer, AbstractMapHandler> HANDLER_MAP = new HashMap<>();

    private static Map<SkillEnum, BaseActionHandler> actionHandlerMap;

    /**
     * 根据地图组id获得处理器 如果没有处理器就用保底地图的
     *
     * @param groupId
     * @param <T>
     * @return
     */
    public static <T extends AbstractMapHandler> T getHandler(int groupId) {
        AbstractMapHandler handler = HANDLER_MAP.get(groupId);
        if (handler == null) {
            handler = HANDLER_MAP.get(MapGroupType.MAIN_CITY.getGroupId());
        }
        return (T)handler;
    }

    /**
     * 获取技能行为处理器
     *
     * @param type
     * @param <T>
     * @return
     */
    public static <T extends BaseActionHandler> T getActionHandler(SkillEnum type) {
        BaseActionHandler handler = actionHandlerMap.get(type);
        return (T)handler;
    }

    /**
     * 获取地图对应的handler
     *
     * @param mapId
     * @param <T>
     * @return
     */
    public static <T extends AbstractMapHandler> T getAbstractMapHandler(int mapId) {
        MiniMapResource miniMapResource = WorldManager.getInstance().getMapResource(mapId);
        if (miniMapResource == null) {
            logger.warn("获取地图处理器失败,[{}]没有对应资源文件", mapId);
            RequestException.throwException(MessageEnum.RESOURCE_NOT_EXIST);
        }
        return AbstractMapHandler.getHandler(miniMapResource.getGroupId());
    }

    @PostConstruct
    private void init() {
        HANDLER_MAP.put(getGroupType().getGroupId(), this);
        actionHandlerMap = SkillEnum.TYPE_TO_HANDLER;
    }

    public boolean canEnterMap(Player player, int mapId) {
        return true;
    }

    public void canEnterMapThrow(Player player, int mapId, long sceneId) {
        if (!canEnterMap(player, mapId)) {
            RequestException.throwException(I18N.MAP_ENTER_CONDITION_NOT_SATISFY);
        }
    }

    /**
     * 进图前置工作[修改玩家信息:玩家切图状态,玩家上一张地图id]
     *
     * @param player
     */
    public final void enterMapPre(Player player) {
        player.setChangingMap(true);
    }

    /**
     * 进入地图后置[修改玩家信息:玩家切图状态,玩家当前地图id]
     *
     * @param player
     * @param currentMapId
     */
    public final void enterMapAfter(Player player, int currentMapId, long currentSceneId) {
        player.setCurrentMapId(currentMapId);
        player.setCurrentSceneId(currentSceneId);
        player.setChangingMap(false);
    }

    /**
     * 退图前置[修改玩家信息:玩家切图状态]
     *
     * @param player
     */
    public final void leaveMapPre(Player player) {
        player.setChangingMap(true);
    }

    /**
     * 退图后置[修改玩家信息:玩家切图状态,玩家当前地图id,玩家上一张地图id]
     *
     * @param player
     */
    public final void leaveMapAfter(Player player) {
        player.setLastMapId(player.getCurrentMapId());
        player.setCurrentMapId(0);
        player.setChangingMap(false);
    }

    /**
     * 打印地图信息
     *
     * @param player
     * @param mapId
     */
    public abstract void doLogMap(Player player, int mapId, long sceneId);

    // 进图失败 自动回到主城
    public void handEnterMapFailed(Player player) {
        ExecutorUtils.submit(EnterMapCommand.valueOf(player, 4, 0));
    }

    @Override
    public void heartBeat(int mapId) {}

    @Override
    public void test(int mapId, long sceneId, Map<String, Object> param) {}

    @Override
    public void showAround(Player player) {
        move(player, getMapScene(player.getCurrentMapId(), player.getCurrentSceneId())
            .getMapObject(player.getPlayerId()).getCurrentGrid());
    }

    /**
     * 广播
     *
     * @param mapId
     * @param currentGrid
     */
    public void broadcast(int mapId, Grid currentGrid) {}

    /**
     * 关闭副本
     *
     * @param mapId
     * @param sceneId
     */
    public void closeInstance(int mapId, long sceneId) {

    }

    /**
     * 处理单位死亡
     *
     * @param unit
     */
    public void handlerUnitDead(BaseCreatureUnit unit) {

    }

    // 预处理sceneId
    public long decodeSceneId(Player player, long sceneId) {
        return sceneId;
    }
}
