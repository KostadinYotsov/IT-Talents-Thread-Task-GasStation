package db;

import java.time.LocalDateTime;

public class Loading implements Comparable<Loading> {
	
	private int kolonkaId;
	private String fuelType;
	private int fuelQuantity;
	private LocalDateTime loadingTime;
	
	public Loading(int kolonkaId, String fuelType, int fuelQuantity, LocalDateTime loadingTime) {
		this.kolonkaId = kolonkaId;
		this.fuelType = fuelType;
		this.fuelQuantity = fuelQuantity;
		this.loadingTime = loadingTime;
	}

	@Override
	public int compareTo(Loading o) {
		return this.loadingTime.compareTo(o.loadingTime);
	}

	@Override
	public String toString() {
		return "Loading [fuelType=" + fuelType + ", fuelQuantity=" + fuelQuantity + ", loadingTime=" + loadingTime
				+ "]";
	}
	
	

}
