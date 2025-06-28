package org.ohs.apitest.domain.concurrency.client;

import org.redisson.api.*;

import java.util.concurrent.locks.Lock;

public interface RedissonClient extends Lock, RLockAsync {
    RLock getLock(String name);
}
