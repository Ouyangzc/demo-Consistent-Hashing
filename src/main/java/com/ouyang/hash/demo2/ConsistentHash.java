package com.ouyang.hash.demo2;

import java.util.Collection;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @ClassName ConsistentHash
 * @Description 一致性hash
 * @Author OuYang
 * @Date 2023/8/21 10:50
 * @Version 1.0
 */
public class ConsistentHash<T extends Node> {

    private final SortedMap<Long, VirtualNode<T>> ring = new TreeMap<>();
    private final HashFunction hashFunction;

    public ConsistentHash(Collection<T> pNodes, int vNodeCount) {
        this(pNodes, vNodeCount, new MD5Hash());
    }

    /**
     * @param pNodes       物理节点集合
     * @param vNodeCount   虚拟节点数
     * @param hashFunction hash算法
     */
    public ConsistentHash(Collection<T> pNodes, int vNodeCount, HashFunction hashFunction) {
        if (hashFunction == null) {
            throw new NullPointerException("Hash Function is null");
        }
        this.hashFunction = hashFunction;
        if (pNodes != null) {
            for (T pNode : pNodes) {
                addNode(pNode, vNodeCount);
            }
        }
    }

    public void addNode(T pNode, int vNodeCount) {
        if (vNodeCount < 0) {
            throw new IllegalArgumentException("illegal virtual node counts :" + vNodeCount);
        }
        //获取已存在的虚拟节点索引
        int existingReplicas = getExistingReplicas(pNode);
        for (int i = 0; i < vNodeCount; i++) {
            VirtualNode<T> vNode = new VirtualNode<>(pNode, i + existingReplicas);
            ring.put(hashFunction.hash(vNode.getKey()), vNode);
        }
    }

    /**
     * 移除节点
     *
     * @param pNode
     */
    public void removeNode(T pNode) {
        Iterator<Long> it = ring.keySet().iterator();
        while (it.hasNext()) {
            Long key = it.next();
            VirtualNode<T> virtualNode = ring.get(key);
            if (virtualNode.isVirtualNodeOf(pNode)) {
                it.remove();
            }
        }
    }

    /**
     * 返回实际节点值
     *
     * @param objectKey
     * @return
     */
    public T routeNode(String objectKey) {
        if (ring.isEmpty()) {
            return null;
        }
        //计算key的hash值
        Long hashVal = hashFunction.hash(objectKey);
        //获取从hash环上的节点集合
        SortedMap<Long, VirtualNode<T>> tailMap = ring.tailMap(hashVal);
        Long nodeHashVal = !tailMap.isEmpty() ? tailMap.firstKey() : ring.firstKey();
        return ring.get(nodeHashVal).getPhysicalNode();
    }

    public int getExistingReplicas(T pNode) {
        int replicas = 0;
        for (VirtualNode<T> vNode : ring.values()) {
            if (vNode.isVirtualNodeOf(pNode)) {
                replicas++;
            }
        }
        return replicas;
    }

}
