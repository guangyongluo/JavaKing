---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by luowei.
--- DateTime: 2024/2/1 12:26
---

--- make judge and delete atomic

if redis.call('get', KEYS[1]) == ARGV[1] then
    return redis.call('del', KEYS[1])
else
    return 0
end

--- v1 lock()

if redis.call('exists', 'key') == 0 then
    redis.call('hset', 'key', 'uuid:threadid', 1)
    redis.call('expire', 'key', 50)
    return 1
elseif redis.call('hexists', 'key', 'uuid:threadid') == 1 then
    redis.call('hincrby', 'key', 'uuid:threadid', 1)
    redis.call('expire', 'key', 50)
    return 1
else
    return 0
end

--- v2 lock()

if redis.call('exists', 'key') == 0 or redis.call('hexists', 'key', 'uuid:threadid') == 1 then
    redis.call('hincrby', 'key', 'uuid:threadid', 1)
    redis.call('expire', 'key', 50)
    return 1
else
    return 0
end

---v3 lock()

if redis.call('exists', KEYS[1]) == 0 or redis.call('hexists', KEYS[1], ARGV[1]) == 1 then
    redis.call('hincrby', KEYS[1], ARGV[1], 1)
    redis.call('expire', KEYS[1], ARGV[2])
    return 1
else
    return 0
end

---v1 unlock()

if redis.call('hexists', 'key', 'uuid:threadid') == 0 then
    return nil
elseif redis.call('hincrby', key, 'uuid:threadid', -1) == 0 then
    return redis.call('del', key)
else
    return 0
end

---v2 unlock()

if redis.call('hexists', KEYS[1], ARGV[1]) == 0 then
    return nil
elseif redis.call('hincrby', KEYS[1], ARGV[1], -1) == 0 then
    return redis.call('del', KEYS[1])
else
    return 0
end

-- auto increase life

if redis.call('hexists', KEYS[1], ARGV[1]) == 1 then
    return redis.call('expire', KEYS[1], ARGV[2])
else
    return 0
end