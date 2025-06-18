package test;
import java.util.Date;


public class Message {
	
	public final byte[] data;
    public final String asText;
    public final double asDouble;
    public final Date date;
    
    public Message(byte[] data) {
        this.data = data;
        this.date = new Date();
        this.asText = new String(data);
        double d;
        try {
            d = Double.parseDouble(asText);
        } catch (Exception e) {
            d = Double.NaN;
        }
        this.asDouble = d;
    }

    public Message(String text) {
        this(text.getBytes());
    }

    public Message(double val) {
        this(Double.toString(val));
    }
}
