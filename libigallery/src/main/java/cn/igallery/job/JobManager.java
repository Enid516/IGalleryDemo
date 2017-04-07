package cn.igallery.job;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Enid on 2016/9/23.
 */
public class JobManager {
    private Queue<Job> jobQueue;
    private boolean queueFree = true;

    public JobManager() {
        jobQueue = new LinkedBlockingDeque<>();
    }

    public void addJob(Job job) {
        if (jobQueue.isEmpty() && queueFree) {
            jobQueue.offer(job);
            start();
        } else {
            jobQueue.offer(job);
        }
    }

    private void start() {
        Observable
                .create(new Observable.OnSubscribe<Job>() {
                    @Override
                    public void call(Subscriber<? super Job> subscriber) {
                        queueFree = false;
                        Job job;
                        while ((job = jobQueue.poll()) != null) {
                            job.onRunJob();
                        }
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Job>() {
                    @Override
                    public void onCompleted() {
                        queueFree = true;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Job job) {

                    }
                });
    }
}
