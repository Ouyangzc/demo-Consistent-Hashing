package com.ouyang.hash.demo2;

/**
 * @ClassName VirtualNode
 * @Description 虚拟节点
 * @Author OuYang
 * @Date 2023/8/21 10:47
 * @Version 1.0
 */
public class VirtualNode<T extends Node> implements Node {

    /**
     * 真实节点
     */
    final T physicalNode;
    /**
     * 虚拟节点索引
     */
    final int replicaIndex;

    public VirtualNode(T physicalNode, int replicaIndex) {
        this.replicaIndex = replicaIndex;
        this.physicalNode = physicalNode;
    }

    @Override
    public String getKey() {
        return physicalNode.getKey() + "-" + replicaIndex;
    }

    public T getPhysicalNode() {
        return physicalNode;
    }

    public boolean isVirtualNodeOf(T pNode) {
        return physicalNode.getKey().equals(pNode.getKey());
    }
}
