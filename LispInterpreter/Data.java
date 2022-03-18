package LispInterpreter;

public class Data {
	
	private Object value;
	private String description;
	
	public Data(Object value, String description) {
		this.value = value;
		this.description = description;
	}
	
	public Data(Object value) {
		this.value = value;
		this.description = null;
	}
	
	public Object getValue() {
		return value;
	}
	
	public String getDescription() {
		return description;
	}
	
	@Override
	public String toString() {
		return value + "";
		
	}

}
