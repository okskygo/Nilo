package com.silver.cat.nilo.config.dagger.activity;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by xiezhenyu on 2017/1/17.
 */


@Qualifier
@Retention(RUNTIME)
public @interface ForActivity {
}
