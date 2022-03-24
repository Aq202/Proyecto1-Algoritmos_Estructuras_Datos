package LispInterpreter;

public class IndeterminateObject {
	
	private Object value;

	public IndeterminateObject(Object value){
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	
}
