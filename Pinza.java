package types;

public class Pinza {
	private int idpinza;
	private int senal;
	private long timeStamp;
	private String ip;
	private String registro;
	
	public Pinza(int idpinza, int senal, long timeStamp, String ip) {
		super();
		this.idpinza = idpinza;
		this.senal = senal;
		this.timeStamp = timeStamp;
		this.ip = ip;
	}

	public Pinza() {
		super();
	}

	public int getIdpinza() {
		return idpinza;
	}

	public void setIdpinza(int idpinza) {
		this.idpinza = idpinza;
	}

	public int getSenal() {
		return senal;
	}

	public void setSenal(int senal) {
		this.senal = senal;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRegistro() {
		return "Apertura de " + senal + " grados.";
	}

	public void setRegistro(String registro) {
		this.registro = "Apertura de " + senal + " grados.";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idpinza;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((registro == null) ? 0 : registro.hashCode());
		result = prime * result + senal;
		result = prime * result + (int) (timeStamp ^ (timeStamp >>> 32));
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
		Pinza other = (Pinza) obj;
		if (idpinza != other.idpinza)
			return false;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (registro == null) {
			if (other.registro != null)
				return false;
		} else if (!registro.equals(other.registro))
			return false;
		if (senal != other.senal)
			return false;
		if (timeStamp != other.timeStamp)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Pinza [idpinza=" + idpinza + ", senal=" + senal + ", timeStamp=" + timeStamp + ", ip=" + ip
				+ ", registro= Apertura de " + senal + " grados]";
	}
	
	
}
