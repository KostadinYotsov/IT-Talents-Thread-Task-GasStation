package gasStation;

import java.time.LocalDateTime;
import java.util.concurrent.ArrayBlockingQueue;

public class Cashier extends Thread {
	
	static GasStation station;
	private ArrayBlockingQueue<Car> kasa;

	public Cashier() {

	}
	
	@Override
	public void run() {
		while (true) {
			try {
				Car car=kasa.take();
				String type=car.getType().toString();
				int amount=car.getAmount();
				int kolonka=car.getKolonka();
				int number=car.getNumber();
				Thread.sleep(5000);
				System.out.println(Thread.currentThread().getName() + " obsluji kola " + number + " koqto si plati za " + amount + " litra " + type);
				station.addDate(kolonka, type, LocalDateTime.now(), amount);
			} catch (InterruptedException e) {
				System.out.println("oops");
			}
		}
		
	}

	public static GasStation getStation() {
		return station;
	}

	public static void setStation(GasStation station) {
		FuelBoy.station = station;
	}

	public void addKasa(ArrayBlockingQueue<Car> kasa) {
		this.kasa=kasa;
		
	}
}
