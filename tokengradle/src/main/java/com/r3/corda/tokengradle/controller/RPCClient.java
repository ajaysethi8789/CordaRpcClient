package com.r3.corda.tokengradle.controller;


import com.r3.corda.lib.tokens.contracts.states.NonFungibleToken;
import net.corda.client.rpc.CordaRPCClient;
import net.corda.core.contracts.ContractState;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.identity.AbstractParty;
import net.corda.core.node.NodeInfo;
import net.corda.core.utilities.NetworkHostAndPort;
import net.corda.core.messaging.DataFeed;
import net.corda.nodeapi.internal.bridging.BridgeControl;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import net.corda.client.rpc.CordaRPCConnection;
import net.corda.core.messaging.CordaRPCOps;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import net.corda.core.node.services.VaultService;
import net.corda.examples.bikemarket.states.*;

import net.corda.client.rpc.CordaRPCClient;
import net.corda.client.rpc.CordaRPCConnection;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.messaging.DataFeed;
import net.corda.core.node.NodeInfo;
import net.corda.core.node.services.Vault;
import net.corda.core.utilities.NetworkHostAndPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import java.util.List;

@RestController
public class RPCClient {

    @RequestMapping(value = "/getNetworkMapSnapshot", method = RequestMethod.GET)
    public List<NodeInfo> getNetworkMapSnapshot(){

        final NetworkHostAndPort nodeAddress = NetworkHostAndPort.parse("192.168.1.9:10006");
        String username = "user1";
        String password = "test";

        final CordaRPCClient client = new CordaRPCClient(nodeAddress);
        final CordaRPCConnection connection = client.start(username, password);
        final CordaRPCOps proxy = connection.getProxy();

        final List<NodeInfo> nodes = proxy.networkMapSnapshot();

        return nodes;
    }

    @RequestMapping(value = "/getAssetById/{dltAssetId}", method = RequestMethod.GET)
    public CreateAssetState getAssetById(@PathVariable("dltAssetId") String dltAssetId){

        System.out.println("getAssetById--> AssetId from client"+dltAssetId);
        final NetworkHostAndPort nodeAddress = NetworkHostAndPort.parse("192.168.1.9:10006");
        String username = "user1";
        String password = "test";

        final CordaRPCClient client = new CordaRPCClient(nodeAddress);
        final CordaRPCConnection connection = client.start(username, password);
        final CordaRPCOps proxy = connection.getProxy();

        //proxy.vaultQueryBy<CreateAssetState>().states.filter { it.state.data.lender.equals(proxy.nodeInfo().legalIdentities.first()) }

        //proxy.vaultQueryBy(<CreateAssetState>().getStates());

        //List<StateAndRef<CreateAssetState>> artStateAndRefs = getServiceHub().getVaultService().queryBy(CreateAssetState.class).getStates();

//        StateAndRef<CreateAssetState> inputArtStateAndRef = artStateAndRefs
//                .stream().filter(artStateAndRef -> {
//                    CreateAssetState artState = artStateAndRef.getState().getData();
//                    return artState.getDltAssetId().equals(dltAssetId);
//                })
//                .findAny()
//                .orElseThrow(() -> new IllegalArgumentException("The piece of art was not found."));
//
//
////        DataFeed<Vault.Page<CreateAssetState>, Vault.Update<CreateAssetState>> dataFeed = proxy.vaultTrack(CreateAssetState.class);
////        Vault.Page<CreateAssetState> snapshot = dataFeed.getSnapshot();
////        return snapshot;
//        return inputArtStateAndRef;



        DataFeed<Vault.Page<CreateAssetState>, Vault.Update<CreateAssetState>> dataFeed = proxy.vaultTrack(CreateAssetState.class);

        //this gives a snapshot of IOUState as of now. so if there are 11 IOUState as of now, this will return 11 IOUState objects
        Vault.Page<CreateAssetState> snapshot = dataFeed.getSnapshot();



        // call a method for each IOUState
        CreateAssetState cs = null;
        System.out.println("all states"+snapshot.getStates());
        for (StateAndRef<CreateAssetState> state : snapshot.getStates()) {
            System.out.println("Inside for loop AssetId from vault" + state.getState().getData().getAssetId());
            if (state.getState().getData().getAssetId().equals(dltAssetId)) {
                cs =  state.getState().getData();
            }
        }
        return cs;

    }

    private static void actionToPerform(StateAndRef<CreateAssetState> state) {
        System.out.println("{}"+ state.getState().getData());
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello(){
        return "Hello";
    }
}
