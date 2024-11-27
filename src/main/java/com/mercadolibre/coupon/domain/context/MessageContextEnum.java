package com.mercadolibre.coupon.domain.context;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class MessageContextEnum<K extends Enum<K>, V> implements MessageContext<K, V> {

    private static final String ERR_MSJ = "Item %s not found in message context.";
    private static final String ERR_MSJ_CAST = "Key item class %s is not valid: Expected %s - Found %s.";

    private Map<K, V> context;

    public MessageContextEnum(Class<K> keyType) {
        context = new EnumMap<>(keyType);
    }

    @Override
    public void addItem(K key, V value) {
        context.put(key, value);
    }

    @Override
    public <T> T getItem(K key, Class<T> targetType) {
        V item = this.existItem(key) ? context.get(key) : null;

        if (Objects.isNull(item)) {
            throw new IllegalArgumentException(String.format(ERR_MSJ, key));
        }

        if (!targetType.isInstance(item)) {
            throw new IllegalArgumentException(String.format(ERR_MSJ_CAST, key, targetType, item.getClass()));
        }

        return targetType.cast(item);
    }

    @Override
    public boolean existItem(K item) {
        return context.containsKey(item);
    }

    @Override
    public void clean() {
        context.clear();
    }

}
