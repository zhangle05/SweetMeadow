/**
 * 
 */
package com.sweetmeadow.api.bridge.tasks;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.DisallowConcurrentExecution;

/**
 * @author zhangle
 *
 */
@DisallowConcurrentExecution
public class SystemScheduledTaskRunner {

    private static final Log logger = LogFactory
            .getLog(SystemScheduledTaskRunner.class);

    private List<AbstractScheduledTask> taskList;

    /**
     * @param taskList
     *            the taskList to set
     */
    public void setTaskList(List<AbstractScheduledTask> taskList) {
        logger.debug(
                "setting system scheduled tasks, size is:" + taskList.size());
        this.taskList = taskList;
    }

    public void runTasks() {
        logger.debug("start running system scheduled tasks...");
        for (AbstractScheduledTask task : taskList) {
            try {
                task.runTask();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        logger.debug("run system scheduled tasks over.");
    }

}
