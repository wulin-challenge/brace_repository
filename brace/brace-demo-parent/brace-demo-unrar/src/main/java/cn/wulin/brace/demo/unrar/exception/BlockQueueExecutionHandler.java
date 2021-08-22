package cn.wulin.brace.demo.unrar.exception;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockQueueExecutionHandler implements RejectedExecutionHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(BlockQueueExecutionHandler.class);

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		BlockingQueue<Runnable> queue = executor.getQueue();
		try {
			queue.put(r);
		} catch (InterruptedException e) {
			String name = Thread.currentThread().getName();
			LOGGER.error("线程{}被中断!",name,e);
		}
	}

}
