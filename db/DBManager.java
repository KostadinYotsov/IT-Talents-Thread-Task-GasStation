package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import gasStation.Car.FuelType;

public class DBManager {
	
	private static final String DB_IP="192.168.8.22";
	private static final  String DB_PORT="3306";
	private static final String DB_NAME="hr";
	private static final  String DB_USER="ittstudent";
	private static final String DB_PASS="ittstudent-123";
	
	private static DBManager instance;
	private static Connection con;
	
	private DBManager () {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Error loading DriverManager " + e.getMessage());
		}
		try {
			con=DriverManager.getConnection("jdbc:mysql://"+DB_IP+ ":" +DB_PORT+ "/" + DB_NAME, DB_USER, DB_PASS);
		} catch (SQLException e) {
			System.out.println("Error connecting to Datebase " + e.getMessage());
		}
	}
	
	public synchronized static DBManager getInstance () {
		if (instance==null) {
			instance=new DBManager();
		}
		return instance;
	}

	
	public void getAllLoading () {
		String sql="SELECT kolonka_id, fuel_type, fuel_quantity, loading-type FROM station_loadings";
		try {
			PreparedStatement st=con.prepareStatement(sql);
			ResultSet rs=st.executeQuery();
			TreeMap<String, TreeSet<Loading>> loadings=new TreeMap<>();
			while (rs.next()) {
				int kolonkaId=rs.getInt("kolonka_id");
				String fuelType=rs.getString("fuel_type");
				int fuelQuantity=rs.getInt("fuel_quantity");
				LocalDateTime loadingTime=rs.getTimestamp("loading_time").toLocalDateTime();
				Loading l=new Loading(kolonkaId, fuelType, fuelQuantity, loadingTime);
				if (loadings.containsKey("Kolonka " + kolonkaId)) {
					loadings.put("Kolonka " + kolonkaId, new TreeSet<>());
				}
				loadings.get("Kolonka " + kolonkaId).add(l);
			}
			for (Entry<String, TreeSet<Loading>> e :loadings.entrySet()) {
				System.out.println(e.getKey() + " :");
				TreeSet<Loading> set=e.getValue();
				for(Loading l : set) {
					System.out.println(l);
				}
			}
		} catch (SQLException e) {
			System.out.println("SQL :" + e.getMessage());
		}
	}
	
	public void insertToDB (int kolonkaId, String fuelType, int fuelQuantity, LocalDateTime date) {
		String sql="INSERT INTO loading_stations (kolonka_id, fuel_type, fuel_quantity, loading-type) VALUES (?,?,?,?)";
		try {
			PreparedStatement st=con.prepareStatement(sql);
			st.setInt(1, kolonkaId);
			st.setString(2, fuelType);
			st.setInt(1, fuelQuantity);
			st.setTimestamp(4, Timestamp.valueOf(date));
			st.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL :" + e.getMessage());
		}
	}
	
	public void countCarsByDay () {
		String sql="SELECT kolonka_id, COUNT(*) AS CountCars FROM station_loadings GROUP BY kolonka_id HAVING DATE(loading_time)=Date.now()";
		try {
			PreparedStatement st=con.prepareStatement(sql);
			ResultSet rs=st.executeQuery();
			TreeMap<String, Integer> map=new TreeMap<>();
			while (rs.next()) {
				int kolonkaId=rs.getInt("kolonka_id");
				int countCars=rs.getInt("CountCars");
				map.put("Kolonka " + kolonkaId, countCars);
				
			}
			for (Entry<String, Integer> e :map.entrySet()) {
				System.out.println(e.getKey() + " :" + e.getValue() + " cars");
			}
		} catch (SQLException e) {
			System.out.println("SQL :" + e.getMessage());
		}
	}
	
	public void totalFuelQuantity () {
		String sql="SELECT fuel_type, SUM(fuel_quantity) AS TotalFuelQuantity FROM station_loadings GROUP BY fuel_type";
		try {
			PreparedStatement st=con.prepareStatement(sql);
			ResultSet rs=st.executeQuery();
			TreeMap<String, Integer> map=new TreeMap<>();
			while (rs.next()) {
				String fuelType=rs.getString("fuel_type");
				int fuelQuantity=rs.getInt("TotalFuelQuantity");
				map.put(fuelType, fuelQuantity);
				
			}
			for (Entry<String, Integer> e :map.entrySet()) {
				System.out.println(e.getKey() + " :" + e.getValue() + " l.");
			}
		} catch (SQLException e) {
			System.out.println("SQL :" + e.getMessage());
		}
	}
	
	public void totalMoneyFomFuel () {
		String sql="SELECT fuel_type, SUM(fuel_quantity) AS TotalFuelQuantity FROM station_loadings GROUP BY fuel_type";
		PreparedStatement st=null;
		ResultSet rs=null;
		try {
			st=con.prepareStatement(sql);
			rs=st.executeQuery();
			TreeMap<String, Double> map=new TreeMap<>();
			while (rs.next()) {
				String fuelType=rs.getString("fuel_type");
				int fuelQuantity=rs.getInt("TotalFuelQuantity");
				double fuel=(double) fuelQuantity;
				if (fuelType.equals(FuelType.DIESEL.toString())) {
					map.put(fuelType, fuelQuantity*2.40);
				}
				if (fuelType.equals(FuelType.LPG.toString())) {
					map.put(fuelType, fuelQuantity*1.60);
				}
				if (fuelType.equals(FuelType.PETROL.toString())) {
					map.put(fuelType, fuelQuantity*2.00);
				}
				
			}
			for (Entry<String, Double> e :map.entrySet()) {
				System.out.println(e.getKey() + " :" + e.getValue() + " lv.");
			}
		} catch (SQLException e) {
			System.out.println("SQL :" + e.getMessage());
		}
		finally {
		
			try {
				rs.close();
				st.close();
			} catch (SQLException e) {
				System.out.println("SQL :" + e.getMessage());
			}
		}
		
	}
	
	public void closeConnection(){
		try {
			con.close();
		} catch (SQLException e) {
			System.out.println("SQL-"+e.getMessage());
		}
	}
}
