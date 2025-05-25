package com.pm.billing_service.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BillingClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();


        BillingServiceGrpc.BillingServiceBlockingStub stub = BillingServiceGrpc.newBlockingStub(channel);

        BillingRequest request = BillingRequest.newBuilder()
                .setPatientId("f40b861a-1269-41f3-97e9-e160202f55d5")
                .setName("Hello")
                .setEmail("Hello")
                .build();

        BillingResponse response = stub.createBillingAccount(request);
        System.out.println("Response: " + response);

        channel.shutdown();
    }
}
