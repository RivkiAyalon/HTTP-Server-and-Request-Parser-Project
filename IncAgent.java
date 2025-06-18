package test;


public class IncAgent implements Agent{

	
	 String[] subs, pubs;
	    String name = "inc";
	    
	   
	    public IncAgent(String[] subs, String[] pubs) {
	        this.subs = subs;
	        this.pubs = pubs;
	        TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();
	        tm.getTopic(subs[0]).subscribe(this);
	        tm.getTopic(pubs[0]).addPublisher(this);
	    }
	    
	    
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void reset() {
		
	}

	@Override
	public void callback(String topic, Message msg) {
		 double val = msg.asDouble;
	        if (!Double.isNaN(val)) {
	            double result = val + 1;
	            TopicManagerSingleton.get().getTopic(pubs[0]).publish(new Message(result));
	        }
		
	}

	@Override
	public void close() {
		  TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();
	        tm.getTopic(subs[0]).unsubscribe(this);
	        tm.getTopic(pubs[0]).removePublisher(this);
		
	}

    
}
