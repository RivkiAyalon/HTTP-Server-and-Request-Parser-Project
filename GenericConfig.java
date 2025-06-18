package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class GenericConfig implements Config{

	
	private String fileName;
    private List<Agent> agents = new ArrayList<>();
	
    public void setConfFile(String fileName) {
        this.fileName = fileName;
    }
    
	@Override
	public void create() {
		try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
            reader.close();
            if (lines.size() % 3 != 0) {
                System.err.println("Invalid config file format");
                return;
            }
            for (int i = 0; i < lines.size(); i += 3) {
                String className = lines.get(i);
                String[] subs = lines.get(i + 1).split(",");
                String[] pubs = lines.get(i + 2).split(",");

                Class<?> clazz = Class.forName(className);
                Agent agent = (Agent) clazz.getConstructor(String[].class, String[].class)
                        .newInstance((Object) subs, (Object) pubs);

                ParallelAgent pa = new ParallelAgent(agent);
                agents.add(pa);
            }
		} catch (Exception e) {
	        e.printStackTrace();
	    }
		
	}

	@Override
	public String getName() {
		 return "Generic Config";
	}

	@Override
	public int getVersion() {
		  return 1;
	}

	@Override
	public void close() {
		  for (Agent a : agents) {
	            a.close();
	        }		
	}


}
