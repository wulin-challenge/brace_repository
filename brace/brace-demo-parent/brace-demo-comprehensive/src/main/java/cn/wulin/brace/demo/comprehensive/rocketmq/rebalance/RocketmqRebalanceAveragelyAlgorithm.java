package cn.wulin.brace.demo.comprehensive.rocketmq.rebalance;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * rocketmq的平均负载均衡算法
 * @author wulin
 *
 */
public class RocketmqRebalanceAveragelyAlgorithm {
	
	public static void main(String[] args) {
		averagelyAlgorithm.printInfo("192.168.0.100@12345");
		averagelyAlgorithm.printInfo("192.168.0.100@12346");
		averagelyAlgorithm.printInfo("192.168.0.100@1237");
	}
	
	/**
	 * 消费者的客户端数据
	 */
	private List<String> cidAll = Arrays.asList(
			"192.168.0.100@12345",
			"192.168.0.100@12346",
			"192.168.0.100@1237");
	
	/**
	 * broker上的消息队列
	 */
	private List<MessageQueue> mqAll = Arrays.asList(
			new MessageQueue("test_topic", "broker-a", 0),
			new MessageQueue("test_topic", "broker-a", 1),
			new MessageQueue("test_topic", "broker-a", 2),
			new MessageQueue("test_topic", "broker-a", 3),
			new MessageQueue("test_topic", "broker-b", 1),
			new MessageQueue("test_topic", "broker-b", 2),
			new MessageQueue("test_topic", "broker-b", 3),
			new MessageQueue("test_topic", "broker-b", 4),
			new MessageQueue("test_topic", "broker-c", 1),
			new MessageQueue("test_topic", "broker-c", 2)
			);
	
	private static RocketmqRebalanceAveragelyAlgorithm averagelyAlgorithm = new RocketmqRebalanceAveragelyAlgorithm(); 
	
	public void printInfo(String currentCID) {
		Collections.sort(mqAll);
		Collections.sort(cidAll);
		List<MessageQueue> allocate = allocate("test_topic_consumer_group", currentCID, mqAll, cidAll);
		
		System.out.println("消费者"+currentCID+"的队列分配: ");
		for (MessageQueue messageQueue : allocate) {
			System.out.println(messageQueue);
		}
		
		System.out.println();
		System.out.println();
	}
	
	/**
     * Allocating by consumer id
     * 
     * <p> 按消费者ID分配
     * <p> 详解: 消息负载算法如果没有特殊的要求,尽量使用AllocateMessageQueueAveragely、AllocateMessageQueueAveragelyByCircle,
     * 因为分配算法比较直观.消息队列分配遵循一个消费者可以分配多个消息队列,但同一个消息队列只会分配给一个消费者,故如果消费者个数大于消息队列数量,
     * 则有些消费者无法消费消息.
     *
     * @param consumerGroup current consumer group - 目前的消费组
     * @param currentCID current consumer id - 当前的消费者ID
     * @param mqAll message queue set in current topic - 当前主题中设置的所有消息队列
     * @param cidAll consumer set in current consumer group - 当前消费者组中的所有消费者
     * @return The allocate result of given strategy - 给定策略的分配结果
     */
    public List<MessageQueue> allocate(String consumerGroup, String currentCID, List<MessageQueue> mqAll,
        List<String> cidAll) {
    	
    	// 校验参数是否正确
        if (currentCID == null || currentCID.length() < 1) {
            throw new IllegalArgumentException("currentCID is empty");
        }
        if (mqAll == null || mqAll.isEmpty()) {
            throw new IllegalArgumentException("mqAll is null or mqAll empty");
        }
        if (cidAll == null || cidAll.isEmpty()) {
            throw new IllegalArgumentException("cidAll is null or cidAll empty");
        }

        List<MessageQueue> result = new ArrayList<MessageQueue>();
        if (!cidAll.contains(currentCID)) {
        	String format = MessageFormat.format("[BUG] ConsumerGroup: {0} The consumerId: {1} not in cidAll: {2}",  consumerGroup,
                  currentCID,
                  cidAll);
        	System.out.println(format);
            return result;
        }

        // 平均分配
        int index = cidAll.indexOf(currentCID); // 第几个consumer。
        int mod = mqAll.size() % cidAll.size(); // 余数，即多少消息队列无法平均分配。
        int averageSize =
            mqAll.size() <= cidAll.size() ? 1 : (mod > 0 && index < mod ? mqAll.size() / cidAll.size()
                + 1 : mqAll.size() / cidAll.size());
        
        // 有余数的情况下，[0, mod) 平分余数，即每consumer多分配一个节点；第index开始，跳过前mod余数。
        int startIndex = (mod > 0 && index < mod) ? index * averageSize : index * averageSize + mod; 
        
        // // 分配队列数量。之所以要Math.min()的原因是，mqAll.size() <= cidAll.size()，部分consumer分配不到消息队列。
        int range = Math.min(averageSize, mqAll.size() - startIndex);
        for (int i = 0; i < range; i++) {
            result.add(mqAll.get((startIndex + i) % mqAll.size()));
        }
        return result;
    }
}
