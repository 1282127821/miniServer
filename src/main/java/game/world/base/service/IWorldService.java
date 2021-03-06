package game.world.base.service;

import game.map.model.Grid;
import game.role.player.model.Player;
import game.world.base.packet.CM_ShowAround;

/**
 * @author : ddv
 * @since : 2019/7/2 下午4:38
 */

public interface IWorldService {
    /**
     * 服务器切图 这里账号,地图线程都会调用
     *
     * @param player
     * @param newMapId
     * @param newSceneId
     * @param clientRequest
     */
    void gatewayChangeMap(Player player, int newMapId, long newSceneId, boolean clientRequest);

    /**
     * 离开地图
     *
     * @param player
     * @param mapId
     * @param clientRequest
     */
    void gatewayLeaveMap(Player player, int mapId, long sceneId, boolean clientRequest);

    /**
     * 离开地图
     *
     * @param player
     * @param mapId
     * @param clientRequest
     */
    void leaveMap(Player player, int mapId, boolean clientRequest);

    /**
     * 打印地图信息
     *
     * @param player
     * @param mapId
     * @param sceneId
     */
    void logMap(Player player, int mapId, long sceneId);

    /**
     * 地图移动 不要带开始坐标了 服务端做寻路
     *
     * @param player
     * @param targetPosition
     */
    void move(Player player, Grid targetPosition);

    /**
     * 查看周围
     *
     * @param player
     * @param request
     */
    void showAround(Player player, CM_ShowAround request);
}
