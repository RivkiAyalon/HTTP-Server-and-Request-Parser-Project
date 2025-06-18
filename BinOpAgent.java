package test;

import java.util.function.BinaryOperator;

public class BinOpAgent implements Agent {
    private final String name;
    private final String input1;
    private final String input2;
    private final String output;
    private final BinaryOperator<Double> operation;

    private double val1 = 0;
    private double val2 = 0;
    private boolean gotVal1 = false;
    private boolean gotVal2 = false;

    public BinOpAgent(String name, String input1, String input2, String output, BinaryOperator<Double> op) {
        this.name = name;
        this.input1 = input1;
        this.input2 = input2;
        this.output = output;
        this.operation = op;

        TopicManagerSingleton.get().getTopic(input1).subscribe(this);
        TopicManagerSingleton.get().getTopic(input2).subscribe(this);
        TopicManagerSingleton.get().getTopic(output).addPublisher(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void reset() {
        val1 = 0;
        val2 = 0;
        gotVal1 = false;
        gotVal2 = false;
    }

    @Override
    public void callback(String topic, Message msg) {
        if (topic.equals(input1)) {
            val1 = msg.asDouble;
            gotVal1 = true;
        } else if (topic.equals(input2)) {
            val2 = msg.asDouble;
            gotVal2 = true;
        }

        if (gotVal1 && gotVal2) {
            double result = operation.apply(val1, val2);
            Message m = new Message(result);
            TopicManagerSingleton.get().getTopic(output).publish(m);
            reset();
        }
    }

    @Override
    public void close() {
    	  TopicManagerSingleton.get().getTopic(input1).unsubscribe(this);
          TopicManagerSingleton.get().getTopic(input2).unsubscribe(this);
          TopicManagerSingleton.get().getTopic(output).removePublisher(this);
    }
}
