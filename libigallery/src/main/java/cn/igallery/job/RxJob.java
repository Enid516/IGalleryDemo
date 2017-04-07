package cn.igallery.job;

/**
 * Created by Enid on 2016/9/23.
 */
public class RxJob {
    private static RxJob INSTANCE;
    JobManager jobManager;
    private RxJob() {
        jobManager = new JobManager();
    }

    public static RxJob getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RxJob();
        }
        return INSTANCE;
    }

    public void addJob(Job job) {
        jobManager.addJob(job);
    }
}
