package gasStation;

public class Car {
	
	public enum FuelType {LPG, PETROL, DIESEL}
	
	private FuelType type;
	private int amount;
	private int kolonka;
	private int number;
	
	public Car(int number) {
		this.number = number;
	}
	
	public FuelType getType() {
		return type;
	}
	public void setType(FuelType type) {
		this.type = type;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getKolonka() {
		return kolonka;
	}
	public void setKolonka(int kolonka) {
		this.kolonka = kolonka;
	}

	public int getNumber() {
		return number;
	}
	
}
