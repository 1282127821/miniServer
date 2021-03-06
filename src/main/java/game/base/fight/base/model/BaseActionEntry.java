package game.base.fight.base.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.base.fight.base.model.attack.ActionTypeEnum;
import game.base.fight.model.pvpunit.BaseCreatureUnit;
import game.base.skill.model.BaseSkill;
import game.world.fight.model.BattleParam;

/**
 * 行为实体类
 *
 * @author : ddv
 * @since : 2019/7/16 3:42 PM
 */

public abstract class BaseActionEntry {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    protected ActionTypeEnum actionType;
    protected BaseSkill baseSkill;
    protected long value;
    protected BaseCreatureUnit caster;
    protected BaseCreatureUnit defender;
    protected ActionResult actionResult;
    protected BattleParam battleParam;

    public BaseActionEntry(BaseCreatureUnit caster, BaseCreatureUnit defender, BaseSkill skill, long value,
        ActionTypeEnum typeEnum, BattleParam battleParam) {
        this.value = value;
        this.actionType = typeEnum;
        this.baseSkill = skill;
        this.caster = caster;
        this.defender = defender;
        this.actionResult = ActionResult.valueOf();
        this.battleParam = battleParam;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    /**
     * 技能生效[伤害,效果...]
     */
    public void doActive() {
        calculate();
        affect();
    }

    public BaseCreatureUnit getCaster() {
        return caster;
    }

    public ActionResult getActionResult() {
        return actionResult;
    }

    public void calculate() {

    }

    public void affect() {
        long currentHp = defender.getCurrentHp();
        long realDamage;
        realDamage = currentHp >= value ? value : currentHp;
        currentHp -= realDamage;
        defender.setCurrentHp(currentHp);
        if (currentHp == 0) {
            defender.handlerDead(this);
        }
        defender.handlerStatus(this);
        actionResult.setId(defender.getId());
        actionResult.setValue(realDamage);
        if (battleParam != null) {
            battleParam.putResult(defender.getId(), actionResult);
        }
        // logger.info("受到伤害[{}] 死亡状态[{}]", realDamage, defender.isDead());
    }

    public BattleParam getBattleParam() {
        return battleParam;
    }
}
