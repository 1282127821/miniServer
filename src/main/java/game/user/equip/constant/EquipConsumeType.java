package game.user.equip.constant;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import game.user.equip.base.consumer.AbstractConsumeProcessor;
import game.user.equip.base.consumer.ItemConsumeProcessor;

/**
 * @author : ddv
 * @since : 2019/6/28 下午2:40
 */

public enum EquipConsumeType {
    /**
     * 道具消耗
     */
    ITEM_CONSUMER("ITEM", ItemConsumeProcessor.class),;

    private static final Map<String, EquipConsumeType> NAME_TO_TYPE = new HashMap<>(EquipConsumeType.values().length);

    static {
        for (EquipConsumeType consumeType : EquipConsumeType.values()) {
            NAME_TO_TYPE.put(consumeType.typeName, consumeType);
        }
    }

    public static EquipConsumeType getConsumer(String typeName) {
        return NAME_TO_TYPE.get(typeName);
    }

    public AbstractConsumeProcessor createProcessor(Map<Long, Integer> consumeParams) {
        Constructor<?>[] constructors = processor.getConstructors();
        try {
            for (Constructor<?> constructor : constructors) {
                if (constructor.getParameterCount() > 0) {
                    Constructor<? extends AbstractConsumeProcessor> processorConstructor =
                        processor.getConstructor(Map.class);

                    return processorConstructor.newInstance(consumeParams);
                } else {
                    return processor.newInstance();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String typeName;
    private Class<? extends AbstractConsumeProcessor> processor;

    EquipConsumeType(String typeName, Class<? extends AbstractConsumeProcessor> processor) {
        this.typeName = typeName;
        this.processor = processor;
    }

}
