package com.mercadolibre.coupon.domain.context;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_CONTEXT_CAST;
import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_CONTEXT_NULL;
import static com.mercadolibre.coupon.crosscutting.utility.MessageUtility.getMessage;
import static java.lang.String.format;

public class MessageContextEnum<K extends Enum<K>, V> implements MessageContext<K, V> {

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
            throw new IllegalArgumentException(format(getMessage(MSJ_CONTEXT_NULL), key));
        }

        if (!targetType.isInstance(item)) {
            throw new IllegalArgumentException(format(getMessage(MSJ_CONTEXT_CAST), key, targetType, item.getClass()));
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
