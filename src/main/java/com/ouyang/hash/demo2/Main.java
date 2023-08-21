package com.ouyang.hash.demo2;

import java.util.Arrays;

/**
 * @ClassName Main
 * @Description 测试工具类
 * @Author OuYang
 * @Date 2023/8/21 10:58
 * @Version 1.0
 */
public class Main {
    public static void main(String[] args) {
        //实例化4个服务节点
        MyServiceNode node1 = new MyServiceNode("IDC1","127.0.0.1",8080);
        MyServiceNode node2 = new MyServiceNode("IDC1","127.0.0.1",8081);
        MyServiceNode node3 = new MyServiceNode("IDC1","127.0.0.1",8082);
        MyServiceNode node4 = new MyServiceNode("IDC1","127.0.0.1",8084);

        //将节点分配到Hash环中，并且每个物理地址有10个虚拟节点
        ConsistentHash<MyServiceNode> consistentHashRouter = new ConsistentHash<>(Arrays.asList(node1,node2,node3,node4),10);

        //模拟5个请求，将5个请求路由到服务节点
        String requestIP1 = "192.168.0.1";
        String requestIP2 = "192.168.0.2";
        String requestIP3 = "192.168.0.3";
        String requestIP4 = "192.168.0.4";
        String requestIP5 = "192.168.0.5";

        goRoute(consistentHashRouter,requestIP1,requestIP2,requestIP3,requestIP4,requestIP5);

        //模拟新增一个服务节点
        MyServiceNode node5 = new MyServiceNode("IDC2","127.0.0.1",8080);
        System.out.println("-------------putting new node online " +node5.getKey()+"------------");
        consistentHashRouter.addNode(node5,10);
        //模拟5个请求，将5个请求路由到服务节点
        goRoute(consistentHashRouter,requestIP1,requestIP2,requestIP3,requestIP4,requestIP5);

        //模拟移除一个服务节点
        consistentHashRouter.removeNode(node3);
        System.out.println("-------------remove node online " + node3.getKey() + "------------");
        goRoute(consistentHashRouter,requestIP1,requestIP2,requestIP3,requestIP4,requestIP5);

    }
    private static void goRoute(ConsistentHash<MyServiceNode> consistentHashRouter ,String ... requestIps){
        for (String requestIp: requestIps) {
            System.out.println(requestIp + " is route to " + consistentHashRouter.routeNode(requestIp));
        }
    }
}

/**
 * 基于请求的一致性hash节点
 */
class MyServiceNode implements Node{
    /**
     * idc机房
     */
    private final String idc;
    /**
     * 物理机ip
     */
    private final String ip;
    /**
     * 端口
     */
    private final int port;

    public MyServiceNode(String idc, String ip, int port) {
        this.idc = idc;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String getKey() {
        return idc + "-"+ip+":"+port;
    }

    @Override
    public String toString() {
        return getKey();
    }
}
