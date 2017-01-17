package com.silver.cat.nilo.util.permission;

import android.app.Activity;
import android.content.Intent;

import com.silver.cat.nilo.config.dagger.activity.ForActivity;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by xiezhenyu on 2017/1/17.
 */

public class PermissionResult {

    private final Activity activity;
    private final Subject<Boolean> subject = PublishSubject.create();

    @Inject
    public PermissionResult(@ForActivity Activity activity) {
        this.activity = activity;
    }

    public Observable<Boolean> request(String... permissions) {
        PermissionHolderActivity.setRequest(new Request(onResultActivity(), permissions));
        Intent intent = new Intent(activity, PermissionHolderActivity.class);
        activity.startActivity(intent);

        return subject;
    }

    private OnResult onResultActivity() {
        return (OnResult) granted -> {
            subject.onNext(granted);
            subject.onComplete();
        };
    }


}
