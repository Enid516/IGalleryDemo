package cn.igallery.job;

/**
 * Created by Enid on 2016/9/23.
 */
public interface Job {
    enum Result {
        SUCCESS,
        FAILURE
    }

    Result onRunJob();
}
