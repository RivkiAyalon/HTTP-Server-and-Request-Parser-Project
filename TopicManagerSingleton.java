package test;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TopicManagerSingleton {
	
	private TopicManagerSingleton() {}
	
	 static class TopicManager {
        private final Map<String, Topic> topicMap = new ConcurrentHashMap<>();

        private TopicManager() {}

        public Topic getTopic(String name) {
            return topicMap.computeIfAbsent(name, Topic::new);
        }

        public Collection<Topic> getTopics() {
            return topicMap.values();
        }

        public void clear() {
            topicMap.clear();
        }
    }

    private static final TopicManager instance = new TopicManager();

    public static TopicManager get() {
        return instance;
    }

	 
}
