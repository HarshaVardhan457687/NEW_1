package com.key_result.key_result_service.util;

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

        // Extract the last 8 digits of the timestamp
        long timestampPart = timestamp % 100000000;  // Last 8 digits

        // Generate a 4-digit random number (ensuring it’s always 4 digits)
        long randomValue = 1000 + new Random().nextInt(9000);  // Random number between 1000 and 9999

        // Combine the timestamp and random value to form a 12-digit unique ID
        return timestampPart * 10000 + randomValue;  // This guarantees a 12-digit ID
    }
}