package com.milchstrabe.rainbow.biz.common;

import com.milchstrabe.rainbow.biz.exception.LogicException;
import com.milchstrabe.rainbow.server.domain.Node;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * @Author ch3ng
 * @Date 2020/5/6 17:02
 * @Version 1.0
 * @Description
 **/
public class ServerNodesCache {

    private static final Set<Node> serverNodes = new TreeSet<>();

    public static synchronized Node getNode() throws LogicException {
        if(serverNodes.isEmpty()){
            throw new LogicException("currently no server nodes are available");
        }
        Node next = serverNodes.iterator().next();
        return next;
    }

    public static synchronized void removeNode(Node node){
        Iterator<Node> iterator = serverNodes.iterator();
        while (iterator.hasNext()){
            Node temp = iterator.next();
            String host = node.getHost();
            int tcpPort = node.getTcpPort();
            String path = host + ":" + tcpPort;

            String tempHost = temp.getHost();
            int tempTcpPort = temp.getTcpPort();
            String tempPath = tempHost + ":" + tempTcpPort;
            if(path.equals(tempPath)){
                iterator.remove();
                break;
            }
        }
    }


    public static synchronized void existUpdateOrAdd(Node node){
        for(Node temp : serverNodes){
            String host = node.getHost();
            int tcpPort = node.getTcpPort();
            String path = host + ":" + tcpPort;

            String tempHost = temp.getHost();
            int tempTcpPort = temp.getTcpPort();
            String tempPath = tempHost + ":" + tempTcpPort;
            if(path.equals(tempPath)){
                temp.setPlayload(node.getPlayload());
                return;
            }
        }
        serverNodes.add(node);
    }
}