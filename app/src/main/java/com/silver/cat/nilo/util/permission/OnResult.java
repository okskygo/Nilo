package com.silver.cat.nilo.util.permission;

import java.io.Serializable;

/**
 * Created by xiezhenyu on 2017/1/17.
 */

interface OnResult extends Serializable {
    void response(boolean granted);
}
