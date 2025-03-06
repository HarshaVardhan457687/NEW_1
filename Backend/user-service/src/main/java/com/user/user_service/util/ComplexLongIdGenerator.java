package com.user.user_service.util;

import org.hibernate.HibernateException;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.io.Serializable;
import java.util.Random;

public class ComplexLongIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        // Get the current timestamp in milliseconds
        long timestamp = System.currentTimeMillis();

        // Generate a 5-digit random number (ensuring it’s always 5 digits)
        long randomValue = 10000 + new Random().nextInt(90000);

        // Combine timestamp and random value to form a unique ID
        return timestamp * 100000 + randomValue;
    }
}