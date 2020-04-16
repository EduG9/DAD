package types;

public class Sensor {
	private int idsensor;
	private long timeStamp;
	private int base;
	private int valor;
	
	
	public Sensor(int idsensor, long timeStamp, int base, int valor) {
		super();
		this.idsensor = idsensor;
		this.timeStamp = timeStamp;
		this.base = base;
		this.valor = valor;
	}
	
	public Sensor() {
		super();
	}

	public int getIdsensor() {
		return idsensor;
	}

	public void setIdsensor(int idsensor) {
		this.idsensor = idsensor;
	}

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + base;
		result = prime * result + idsensor;
		result = prime * result + (int) (timeStamp ^ (timeStamp >>> 32));
		result = prime * result + valor;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sensor other = (Sensor) obj;
		if (base != other.base)
			return false;
		if (idsensor != other.idsensor)
			return false;
		if (timeStamp != other.timeStamp)
			return false;
		if (valor != other.valor)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Sensor [idsensor=" + idsensor + ", TimeStamp=" + timeStamp + ", Base=" + base + ", valor=" + valor 
				+ "]";
	}
	
	
	
}
