package com.silver.cat.nilo.util.permission;

/**
 * Created by xiezhenyu on 2017/1/18.
 */

class Request {

    private final OnResult onResult;
    private final String[] permissions;

    public Request(OnResult onResult, String[] permissions) {
        this.onResult = onResult;
        this.permissions = permissions;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public OnResult getOnResult() {
        return onResult;
    }
}
