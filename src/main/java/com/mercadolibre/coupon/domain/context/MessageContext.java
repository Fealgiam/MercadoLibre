package com.mercadolibre.coupon.domain.context;

public interface MessageContext <K, V> {

    void addItem(K item, V value);

    <T> T getItem(K item, Class<T> targetType);

    boolean existItem(K item);

    void clean();

}
