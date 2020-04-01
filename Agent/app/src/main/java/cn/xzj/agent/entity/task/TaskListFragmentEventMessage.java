package cn.xzj.agent.entity.task;

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/10/8
 * @ Des
 */
public class TaskListFragmentEventMessage {
    public TaskListFragmentEventMessage(TaskDateInfo taskDateInfo) {
        this.taskDateInfo = taskDateInfo;
    }

    private TaskDateInfo taskDateInfo;

    public TaskDateInfo getTaskDateInfo() {
        return taskDateInfo;
    }

    public void setTaskDateInfo(TaskDateInfo taskDateInfo) {
        this.taskDateInfo = taskDateInfo;
    }
}
