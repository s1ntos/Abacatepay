package com.api.abacatepay.dto;

public class AbacatePayResponse {
    public Data data;
    public String error;

    public static class Data {
        public String id;
        public String url;
        public String brCode;
        public String brCodeBase64;
        public String status;
        public String expiresAt;
    }
}
