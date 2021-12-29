package com.epam.learn.nosql.taskmanager.dao;

import java.time.ZonedDateTime;
import java.util.function.Supplier;

public interface NowDateSupplier {
    Supplier<ZonedDateTime> getNowDateSupplier();
    void setNowDateSupplier(Supplier<ZonedDateTime> nowDateSupplier);
}
