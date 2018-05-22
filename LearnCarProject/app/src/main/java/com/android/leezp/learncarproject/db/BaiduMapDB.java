package com.android.leezp.learncarproject.db;

public class BaiduMapDB {
    public Result result;

    public static class Result {

        public AddressComponent addressComponent;

        public static class AddressComponent {
            public String city;
        }
    }
}
