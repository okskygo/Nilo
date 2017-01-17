package com.silver.cat.nilo.config.dagger.activity;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by xiezhenyu on 2017/1/17.
 */

@Scope
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ActivityScope {
}
