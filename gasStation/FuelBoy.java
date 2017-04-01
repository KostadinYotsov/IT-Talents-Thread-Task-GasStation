package gasStation;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import gasStation.Car.FuelType;

public class FuelBoy extends Thread {
	
	static GasStation station;


	@Override
	public void run() {
		while (true) {
			for (int i=0; i<station.getKolonki().size(); i++) {
				Queue<Car> kolonka =station.getKolonki().get(i);
				Car car;
				synchronized (kolonka) {
					if (kolonka.isEmpty()) {
						continue;
					}
					car=kolonka.poll();
				}
				System.out.println(Thread.currentThread().getName() + " vze kola " + car.getNumber()+ " i i sipva benzin.");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					System.out.println("oops");
				}
				FuelType type=FuelType.values()[new Random().nextInt(FuelType.values().length)];
				int amount=new Random().nextInt(31)+10;
				car.setAmount(amount);
				car.setType(type);
				car.setKolonka(i+1);
				station.alignToPay(car);
			}
		}
	}

	public static GasStation getStation() {
		return station;
	}

	public static void setStation(GasStation station) {
		FuelBoy.station = station;
	}

	
}
