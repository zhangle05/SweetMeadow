/**
 * 
 */
package com.sweetmeadow.api.bridge.tasks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.DisallowConcurrentExecution;

/**
 * 卡更新定时任务
 *
 * @author zhangle
 *
 */
@DisallowConcurrentExecution
public class CardsUpdateTask extends AbstractScheduledTask {

    protected final Log LOG = LogFactory.getLog(CardsUpdateTask.class);

    @Override
    public void runTask() {
        update();
    }

    private void update() {
        LOG.info("SCHEDULED TASK: update live status");
    }

}
