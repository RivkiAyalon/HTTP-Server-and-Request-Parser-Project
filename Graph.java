package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Graph extends ArrayList<Node> {
	private static final long serialVersionUID = 1L;
	
    public boolean hasCycles() {
        for (Node node : this) {
            if (node.hasCycles()) return true;
        }
        return false;
    }

    public void createFromTopics() {
        Map<String, Node> nodes = new HashMap<>();
        TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();

        for (Topic topic : tm.getTopics()) {
            String topicName = "T" + topic.name;
            Node topicNode = nodes.computeIfAbsent(topicName, Node::new);
            for (Agent agent : topic.getSubscribers()) {
                String agentName = "A" + agent.getName();
                Node agentNode = nodes.computeIfAbsent(agentName, Node::new);
                topicNode.addEdge(agentNode);
            }
        }

        for (Topic topic : tm.getTopics()) {
            for (Agent agent : topic.getPublishers()) {
                String agentName = "A" + agent.getName();
                Node agentNode = nodes.computeIfAbsent(agentName, Node::new);
                String topicName = "T" + topic.name;
                Node topicNode = nodes.computeIfAbsent(topicName, Node::new);
                agentNode.addEdge(topicNode);
            }
        }

        this.clear();
        this.addAll(nodes.values());
    }
}
