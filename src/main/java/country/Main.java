package country;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	private final static String DB_URL = "jdbc:mysql://localhost:3306/db_nations";
	private final static String DB_USER = "root";
	private final static String DB_PASSWORD = "1234gatto1";

	public static void main(String[] args) throws SQLException {

		Scanner scan = new Scanner(System.in);

		try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

			Country country = selectCountryById(con, scan);
			if (country != null) {
				System.out.println("You've choosen " + country.getName());
				System.out.println("Area: " + country.getArea() + " square KM");
				if (country.getNationalDay() == null && country.getCountryId() == 107) {
					System.out.println("National day: 17-03-1861");
				} else {
					System.out.println("National day: " + country.getNationalDay());
				}
			}
			updateCountry(con, country, scan);
			if (country != null) {
				System.out.println("Dati della riga dopo l'update:");
				System.out.print(country.getCountryId() + "");
				System.out.print(country.getName() + "");
				System.out.print(country.getArea() + "");
				System.out.print(country.getNationalDay() + "");
				System.out.print(country.getCountryCode2() + "");
				System.out.print(country.getCountryCode3() + "");
				System.out.println(country.getRegionId() + "");
			}
		}

		scan.close();

	}

	private static Country selectCountryById(Connection con, Scanner scan) throws SQLException {

		Country country = null;

		System.out.print("Insert your Id: ");
		int UserId = scan.nextInt();
		String sql = "select * from countries c where country_id = ?;";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, UserId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					country = new Country(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getDate(4), rs.getString(5),
							rs.getString(6), rs.getInt(7));
				}
			}

		}

		return country;
	}

	private static void updateCountry(Connection con, Country country, Scanner scan) throws SQLException {
		String update = "UPDATE countries SET country_id=?, name=?, area=?, national_day=?, country_code2=?,"
				+ "country_code3=?, region_id=? WHERE country_id=?;";
		try (PreparedStatement psUpdate = con.prepareStatement(update)) {
			psUpdate.setInt(1, country.getCountryId());
			psUpdate.setString(2, country.getName());
			psUpdate.setDouble(3, country.getArea());
			psUpdate.setDate(4, country.getNationalDay());
			psUpdate.setString(5, country.getCountryCode2());
			psUpdate.setString(6, country.getCountryCode3());
			psUpdate.setInt(7, country.getRegionId());
			psUpdate.setInt(8, country.getCountryId());
			int result = psUpdate.executeUpdate();
			if (result == 0) {
				System.out.println("Country not in list!");
			}

			Country country1 = selectCountryById(con, scan);
			
		}
	}

}
