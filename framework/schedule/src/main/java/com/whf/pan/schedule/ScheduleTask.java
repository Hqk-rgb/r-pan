package com.whf.pan.schedule;

/**
 * @author whf
 * @className ScheduleTask
 * @description 通过实现这个接口，并提供 getName() 方法的实现来具体化其行为。
 * 由于它继承了 Runnable 接口，因此实现了这个接口的类可以被用作线程，可以通过调用 run() 方法来执行相应的任务逻辑。
 * @date 2023/10/29 14:25
 */
public interface ScheduleTask extends Runnable{
    /**
     * 获取定时任务的名称
     * @return
     */
    String getName();
}
