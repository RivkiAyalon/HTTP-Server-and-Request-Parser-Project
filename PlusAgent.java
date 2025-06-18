package test;


public class PlusAgent implements Agent{

	 double x = 0, y = 0;
	 
	 String[] subs, pubs;
	 String name = "plus";
	 
	 public PlusAgent(String[] subs, String[] pubs) {
	        this.subs = subs;
	        this.pubs = pubs;

	        TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();
	        tm.getTopic(subs[0]).subscribe(this);
	        tm.getTopic(subs[1]).subscribe(this);
	        tm.getTopic(pubs[0]).addPublisher(this);
	    }
	    
	@Override
	public String getName() {
		
		return name;
	}

	@Override
	public void reset() {
		 x = 0;
	     y = 0;
		
	}

	@Override
	public void callback(String topic, Message msg) {
		if (topic.equals(subs[0])) {
            x = msg.asDouble;
        } else if (topic.equals(subs[1])) {
            y = msg.asDouble;
        }

        if (!Double.isNaN(x) && !Double.isNaN(y)) {
            double sum = x + y;
            TopicManagerSingleton.get().getTopic(pubs[0]).publish(new Message(sum));
        }
		
	}

	
	@Override
	public void close() {
		TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();
        tm.getTopic(subs[0]).unsubscribe(this);
        tm.getTopic(subs[1]).unsubscribe(this);
        tm.getTopic(pubs[0]).removePublisher(this);
		
	}

}
