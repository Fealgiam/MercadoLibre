package com.mercadolibre.coupon.infrastructure.outputpoint.jpa.repository;

import org.h2.api.Trigger;

import java.sql.Connection;
import java.util.Objects;

public class TriggerUpdateNumberRedeemed implements Trigger {

    @Override
    public void fire(Connection connection, Object[] oldRow, Object[] newRow) {
        if (Objects.isNull(oldRow)) {
            newRow[2] = 1l;
        } else {
            newRow[2] = ((Long) oldRow[2]) + 1l;
        }
    }

}
