package gasStation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import db.DBManager;
import gasStation.Car.FuelType;

public class GasStation {

	private static class Reporter extends Thread{
		private  static DBManager dbManger;
		
		public Reporter() {
			this.dbManger=DBManager.getInstance();
		}
		
		
		@Override
		public void run() {
			dbManger.getAllLoading();
			dbManger.countCarsByDay();
			dbManger.countCarsByDay();
			dbManger.totalMoneyFomFuel();
			try {
				Thread.sleep(1000*10);//sleep for 10 sec
			} catch (InterruptedException e) {
				System.out.println("reporter was interrupted");
			}
		}
	}
	
	Cashier penka=new Cashier();
	Cashier ganka=new Cashier();
	FuelBoy pesho=new FuelBoy();
	FuelBoy gosho=new FuelBoy();
	
	private ArrayList<Queue<Car>> kolonki = new ArrayList<>();
	private ArrayList<ArrayBlockingQueue<Car>> kasi = new ArrayList<>();
	HashMap<Integer, HashMap<String, ConcurrentHashMap<LocalDateTime, Integer>>> statistics=new HashMap<>();
	
	public GasStation() {
		for (int i=0; i<5; i++) {
			kolonki.add(new LinkedList<>());
			statistics.put(i+1, new HashMap<>());
			statistics.get(i+1).put(FuelType.DIESEL.toString(), new ConcurrentHashMap<>());
			statistics.get(i+1).put(FuelType.PETROL.toString(), new ConcurrentHashMap<>());
			statistics.get(i+1).put(FuelType.LPG.toString(), new ConcurrentHashMap<>());
		}
		for (int i=0; i<2; i++) {
			kasi.add(new ArrayBlockingQueue<>(5));	
		}
		penka.addKasa(kasi.get(0));
		ganka.addKasa(kasi.get(1));
		FuelBoy.station=this;
		Cashier.station=this;
		penka.start();
		ganka.start();
		gosho.start();
		pesho.start();
		Reporter rep=new Reporter();
		rep.setDaemon(true);
		rep.start(); 
		
	}
	
	public void enterCar (Car c) {
		kolonki.get(new Random().nextInt(kolonki.size())).offer(c);
	}
	
	public List<Queue<Car>> getKolonki() {
		return Collections.unmodifiableList(kolonki);
	}

	public void alignToPay(Car car) {
		kasi.get(new Random().nextInt(kasi.size())).offer(car);
		
	}
	
	
	
	public void addDate(int kolonka, String type, LocalDateTime date, int amount) {
		statistics.get(kolonka).get(type).put(date, amount);
		
	}
	
	public void printStatistics () {
		for (Integer i :statistics.keySet()) {
			System.out.println("Kolonka N:" + i);
			for (String type: statistics.get(i).keySet()) {
				System.out.println("    -" + type);
				for (Entry<LocalDateTime, Integer> e : statistics.get(i).get(type).entrySet()) {
					System.out.println("        -" + e.getValue() + " litra -" + e.getKey());
				}
			}
		}
	}
	
	
}
