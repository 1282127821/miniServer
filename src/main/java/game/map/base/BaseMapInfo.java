package game.map.base;

import game.world.base.resource.MapBlockResource;
import game.world.base.resource.MiniMapResource;
import game.world.base.service.WorldManager;

/**
 * 地图数据信息[id,地图资源,阻挡点资源等...]
 *
 * @author : ddv
 * @since : 2019/7/10 下午11:14
 */

public abstract class BaseMapInfo<T extends AbstractMovableScene> {
    // 地图id
    protected int mapId;
    // 无分线逻辑 每张地图只会有一张
    protected T mapScene;
    // 地图资源
    protected MiniMapResource miniMapResource;
    // 阻挡点资源
    protected MapBlockResource blockResource;

    /**
     * 初始化地图信息
     *
     * @param mapResource
     * @return
     */
    public void init(MiniMapResource mapResource) {
        mapId = mapResource.getMapId();
        miniMapResource = mapResource;
        blockResource = WorldManager.getInstance().getBlockResource(mapResource.getMapDataConfigId());
        initScene();
    }

    /**
     * 初始化场景容器
     *
     */
    protected abstract void initScene();

    public int getMapId() {
        return mapId;
    }

    public T getMapScene() {
        return mapScene;
    }

    public void setMapScene(T mapScene) {
        this.mapScene = mapScene;
    }

    public T getMapScene(long sceneId) {
        return null;
    }

    public MiniMapResource getMiniMapResource() {
        return miniMapResource;
    }

    public MapBlockResource getBlockResource() {
        return blockResource;
    }
}
