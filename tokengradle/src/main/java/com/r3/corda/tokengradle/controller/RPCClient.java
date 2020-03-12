package com.r3.corda.tokengradle.controller;


import net.corda.client.rpc.CordaRPCClient;
import net.corda.core.node.NodeInfo;
import net.corda.core.utilities.NetworkHostAndPort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import net.corda.client.rpc.CordaRPCConnection;
import net.corda.core.messaging.CordaRPCOps;

import java.util.List;

@RestController
public class RPCClient {

    @RequestMapping(value = "/getNetworkMapSnapshot", method = RequestMethod.GET)
    public List<NodeInfo> getNetworkMapSnapshot(){

        final NetworkHostAndPort nodeAddress = NetworkHostAndPort.parse("ip:port");
        String username = "username";
        String password = "password";

        final CordaRPCClient client = new CordaRPCClient(nodeAddress);
        final CordaRPCConnection connection = client.start(username, password);
        final CordaRPCOps proxy = connection.getProxy();

        final List<NodeInfo> nodes = proxy.networkMapSnapshot();

        return nodes;
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello(){
        return "Hello";
    }
}
