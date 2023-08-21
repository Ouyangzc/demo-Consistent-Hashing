package com.ouyang.hash.demo2;

/**
 * @ClassName HashFunction
 * @Description hash函数接口
 * @Author OuYang
 * @Date 2023/8/21 10:30
 * @Version 1.0
 */
public interface HashFunction {
    /**
     * 根据key计算hash值
     * @param key
     * @return
     */
    long hash(String key);
}
