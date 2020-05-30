package com.on;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.grpc.ManagedChannel;
import io.grpc.alts.ComputeEngineChannelBuilder;
import io.grpc.lookup.v1.RouteLookupRequest;
import io.grpc.lookup.v1.RouteLookupResponse;
import io.grpc.lookup.v1.RouteLookupServiceGrpc;
import java.util.Map;

public class DirectPathRlsTest {

  public static void main(String[] args) {
    String target = "test-bigtablerls.sandbox.googleapis.com";
    System.out.println("connecting to target: " + target);
    ManagedChannel channel =
        ComputeEngineChannelBuilder.forTarget(target)
            .defaultServiceConfig(serviceConfig())
            .disableServiceConfigLookUp()
            .build();

    RouteLookupResponse response =
        RouteLookupServiceGrpc
            .newBlockingStub(channel)
            .routeLookup(
                RouteLookupRequest.newBuilder()
                    .setTargetType("grpc")
                    .setPath("path")
                    .setServer("foo.google.com")
                    .putKeyMap("foo", "bar")
                    .build());

    System.out.println("Response: " + response);
  }

  private static Map<String, Object> serviceConfig() {
    ImmutableMap<String, Object> pickFirstStrategy =
        ImmutableMap.of("pick_first", ImmutableMap.of());

    ImmutableMap<String, Object> childPolicy =
        ImmutableMap.of("childPolicy", ImmutableList.of(pickFirstStrategy));

    ImmutableMap<String, Object> grpcLbPolicy =
        ImmutableMap.of("grpclb", childPolicy);

    ImmutableMap<String, Object> loadBalancingConfig =
        ImmutableMap.of("loadBalancingConfig", ImmutableList.of(grpcLbPolicy));

    return loadBalancingConfig;
  }
}