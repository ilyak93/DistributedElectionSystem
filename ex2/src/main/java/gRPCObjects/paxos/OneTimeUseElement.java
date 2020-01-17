package gRPCObjects.paxos;

public class OneTimeUseElement<T> {
	
	private T element;
	private boolean used;
	
	public OneTimeUseElement(T element){
		this.element = element;
		this.used = false;
	}
	
	public T getElement() {
		return this.element;
	}
	
	public boolean isUsed() {
		return this.used;
	}
	
	public void use() {
		this.used = true;
	}
}
