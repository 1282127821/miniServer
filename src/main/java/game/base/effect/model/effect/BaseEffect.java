package game.base.effect.model.effect;

import game.base.effect.model.BuffContext;

/**
 * @author : ddv
 * @since : 2019/7/23 5:56 PM
 */

public abstract class BaseEffect {

    /**
     * 效果生效
     *
     * @param buffContext
     */
    public abstract void active(BuffContext buffContext);

}
