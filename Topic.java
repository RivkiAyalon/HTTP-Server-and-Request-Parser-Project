package test;

import java.util.ArrayList;
import java.util.List;

public class Topic {

	public final String name;
    private List<Agent> subs = new ArrayList<>();
    private List<Agent> pubs = new ArrayList<>();
    
    Topic(String name) {
        this.name = name;
    }
    
    public void subscribe(Agent agent) {
        if (!subs.contains(agent)) {
            subs.add(agent);
        }
    }
    
    public void unsubscribe(Agent agent) {
        subs.remove(agent);
    }
    
    public void publish(Message m1)
    {
    	for (Agent agent : new ArrayList<>(subs)) {
    	
    		agent.callback(name, m1);
    		}
    	    
    }
    public void addPublisher(Agent agent) {
        if (!pubs.contains(agent)) {
            pubs.add(agent);
        }
    }

    public void removePublisher(Agent agent) {
        pubs.remove(agent);
    }
    public List<Agent> getSubscribers() {
        return subs;
    }

    public List<Agent> getPublishers() {
        return pubs;
    }
}
