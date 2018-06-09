package com.amazonbyod.mysql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.amazonbyod.awsprop.AWSProjectProperties;
import com.amazonbyod.listclass.CompanyProducts;
import com.amazonbyod.listclass.CompanyProfile;
import com.amazonbyod.listclass.Companyannouncements;

/**
 * @author abhinandan
 *
 */
public class MySQLConnection {

	final AWSProjectProperties prop = new AWSProjectProperties();

	/**
	 * @return
	 */
	public Connection mysqlConnect() {
		Connection connect = null;
		String DB_NAME = "";
		try {
			final String JDBC_DRIVER = prop.getMysql_JDBC_DRIVER();
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			DB_NAME = prop.getMysql_dbname();
			final String DB_URL = prop.getMysql_DB_URL() + DB_NAME + "?user=" + prop.getMysql_username() + "&password="
					+ prop.getMysql_password();
			System.out.println(DB_URL);
			connect = DriverManager.getConnection(DB_URL);
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return connect;

	}

	/**
	 * @param connect
	 * @return
	 */
	public int mysqlDisconnect(Connection connect) {
		int flag = 0;
		if (connect != null) {
			try {
				connect.close();
				flag = 1;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			flag = 0;
		}
		return flag;

	}

	public java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
		return new java.sql.Date(date.getTime());
	}

	public boolean tableExits(Connection conn, String tableName) {
		boolean flag = false;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SHOW TABLES LIKE '%" + tableName.toLowerCase().trim() + "%'");
			if (rs.next()) {
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;

	}

	public boolean createTableCompanyMaster(Connection conn) {
		boolean flag = false;
		String tableSchema = "create table if not EXISTS company_master (company_id int PRIMARY KEY,company_name varchar(255),company_symbol varchar(255),"
				+ "company_address text,company_foundedon date,company_ceo varchar(255),comapny_assets double,company_revenue double)";
		try {
			Statement stmt = conn.createStatement();
			if (stmt.executeUpdate(tableSchema) > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;

	}

	/**
	 * @param conn
	 * @return
	 */
	public boolean createTableCompanyProduct(Connection conn) {
		boolean flag = false;
		String tableSchema = "CREATE TABLE IF NOT EXISTS company_product(" + "product_id int PRIMARY KEY ,"
				+ "company_id int," + "product_name varchar( 255 ) ," + "product_description text,"
				+ "product_type varchar( 255 ) ," + "product_initialrelease date," + "product_marketvol double,"
				+ "product_manufacture_lat float," + "product_manufacture_long float,"
				+ "product_manufacture_loc varchar( 255 )" + ")";
		System.out.println(tableSchema);
		try {
			Statement stmt = conn.createStatement();
			if (stmt.executeUpdate(tableSchema) > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;

	}

	/**
	 * @param conn
	 * @return
	 */
	public boolean createTableCompanyAnnouncement(Connection conn) {
		boolean flag = false;
		String tableSchema = "CREATE TABLE IF NOT EXISTS company_announcement(" + "announcemnt_id int PRIMARY KEY ,"
				+ "company_id int," + "announcemnt_date date," + "announcemnt_title varchar( 255 ) ,"
				+ "announcemnt_by varchar( 255 ) ," + "announcemnt_from varchar( 255 ) ," + "announcemnt_path text"
				+ ")";
		try {
			Statement stmt = conn.createStatement();
			if (stmt.executeUpdate(tableSchema) > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;

	}

	/**
	 * @param conn
	 * @param tableName
	 * @return
	 */
	public boolean truncateTable(Connection conn, String tableName) {
		boolean flag = false;
		try {
			Statement statement = conn.createStatement();
			int result = statement.executeUpdate("delete from " + tableName);
			if (result > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @param conn
	 * @param row
	 */
	public void insertDataCompanyMaster(Connection conn, List<CompanyProfile> row) {
		String insertTableSQL = "INSERT INTO `company_master`(`company_id`, `company_name`, `company_symbol`, `company_address`, `company_foundedon`, `company_ceo`, `comapny_assets`, `company_revenue`) "
				+ "VALUES (?,?,?,?,?,?,?,?)";

		if (!tableExits(conn, "company_master")) {
			createTableCompanyMaster(conn);

			try {
				PreparedStatement preparedStatement = conn.prepareStatement(insertTableSQL);
				preparedStatement.setInt(1, Integer.parseInt(row.get(0).getCompanyId()));
				preparedStatement.setString(2, row.get(0).getComapanyName());
				preparedStatement.setString(3, row.get(0).getCompanySymbol());
				preparedStatement.setString(4, row.get(0).getCompanyAddress());
				preparedStatement.setDate(5, convertJavaDateToSqlDate(row.get(0).getCompany_foundedon()));
				preparedStatement.setString(6, row.get(0).getCompany_ceo());
				preparedStatement.setDouble(7, Double.parseDouble(row.get(0).getCompany_assets()));
				preparedStatement.setDouble(8, Double.parseDouble(row.get(0).getCompany_revenue()));

				preparedStatement.executeUpdate();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			truncateTable(conn, "company_master");
			try {
				PreparedStatement preparedStatement = conn.prepareStatement(insertTableSQL);
				preparedStatement.setInt(1, Integer.parseInt(row.get(0).getCompanyId()));
				preparedStatement.setString(2, row.get(0).getComapanyName());
				preparedStatement.setString(3, row.get(0).getCompanySymbol());
				preparedStatement.setString(4, row.get(0).getCompanyAddress());
				preparedStatement.setDate(5, convertJavaDateToSqlDate(row.get(0).getCompany_foundedon()));
				preparedStatement.setString(6, row.get(0).getCompany_ceo());
				preparedStatement.setDouble(7, Double.parseDouble(row.get(0).getCompany_assets()));
				preparedStatement.setDouble(8, Double.parseDouble(row.get(0).getCompany_revenue()));

				preparedStatement.executeUpdate();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/**
	 * @param conn
	 * @param row
	 */
	public void insertDataCompanyProduct(Connection conn, List<CompanyProducts> row) {
		String insertTableSQL = "INSERT INTO `company_product`(`product_id`, `company_id`, `product_name`, `product_description`, `product_type`, `product_initialrelease`, `product_marketvol`, `product_manufacture_lat`, `product_manufacture_long`, `product_manufacture_loc`) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?)";

		if (!tableExits(conn, "company_product")) {
			createTableCompanyProduct(conn);

			try {
				PreparedStatement preparedStatement = conn.prepareStatement(insertTableSQL);
				for (int i = 0; i < row.size(); i++) {
					preparedStatement.setInt(1, Integer.parseInt(row.get(i).getProductId()));
					preparedStatement.setInt(2, Integer.parseInt(row.get(i).getCompanyID()));
					preparedStatement.setString(3, row.get(i).getProduct_name());
					preparedStatement.setString(4, row.get(i).getProduct_description());
					preparedStatement.setString(5, row.get(i).getProduct_type());
					preparedStatement.setDate(6, convertJavaDateToSqlDate(row.get(i).getProduct_initaldate()));
					preparedStatement.setInt(7, row.get(i).getMarketvol());
					preparedStatement.setFloat(8, row.get(i).getLat());
					preparedStatement.setFloat(9, row.get(i).getLng());
					preparedStatement.setString(10, row.get(i).getProduct_loc());

					preparedStatement.executeUpdate();

				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {

			truncateTable(conn, "company_product");

			try {
				PreparedStatement preparedStatement = conn.prepareStatement(insertTableSQL);
				for (int i = 0; i < row.size(); i++) {
					preparedStatement.setInt(1, Integer.parseInt(row.get(i).getProductId()));
					preparedStatement.setInt(2, Integer.parseInt(row.get(i).getCompanyID()));
					preparedStatement.setString(3, row.get(i).getProduct_name());
					preparedStatement.setString(4, row.get(i).getProduct_description());
					preparedStatement.setString(5, row.get(i).getProduct_type());
					preparedStatement.setDate(6, convertJavaDateToSqlDate(row.get(i).getProduct_initaldate()));
					preparedStatement.setInt(7, row.get(i).getMarketvol());
					preparedStatement.setFloat(8, row.get(i).getLat());
					preparedStatement.setFloat(9, row.get(i).getLng());
					preparedStatement.setString(10, row.get(i).getProduct_loc());

					preparedStatement.executeUpdate();

				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/**
	 * @param conn
	 * @param row
	 */
	public void insertDataCompanyAnnouncement(Connection conn, List<Companyannouncements> row) {

		String insertTableSQL = "INSERT INTO `company_announcement`(`announcemnt_id`, `company_id`, `announcemnt_date`, `announcemnt_title`, `announcemnt_by`, `announcemnt_from`, `announcemnt_path`) "
				+ "VALUES (?,?,?,?,?,?,?)";

		if (!tableExits(conn, "company_announcement")) {
			createTableCompanyAnnouncement(conn);
			try {

				PreparedStatement preparedStatement = conn.prepareStatement(insertTableSQL);
				for (int i = 0; i < row.size(); i++) {
					preparedStatement.setInt(1, Integer.parseInt(row.get(i).getAnnouncementId()));
					preparedStatement.setInt(2, Integer.parseInt(row.get(i).getCompanyID()));
					preparedStatement.setDate(3, convertJavaDateToSqlDate(row.get(i).getAnnouncementDate()));
					preparedStatement.setString(4, row.get(i).getAnnouncemnType());
					preparedStatement.setString(5, row.get(i).getAnnouncemnBy());
					preparedStatement.setString(6, row.get(i).getAnnouncemnFrom());
					preparedStatement.setString(7, "");

					preparedStatement.executeUpdate();
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			try {
				truncateTable(conn, "company_announcement");
				
				PreparedStatement preparedStatement = conn.prepareStatement(insertTableSQL);
				for (int i = 0; i < row.size(); i++) {
					preparedStatement.setInt(1, Integer.parseInt(row.get(i).getAnnouncementId()));
					preparedStatement.setInt(2, Integer.parseInt(row.get(i).getCompanyID()));
					preparedStatement.setDate(3, convertJavaDateToSqlDate(row.get(i).getAnnouncementDate()));
					preparedStatement.setString(4, row.get(i).getAnnouncemnType());
					preparedStatement.setString(5, row.get(i).getAnnouncemnBy());
					preparedStatement.setString(6, row.get(i).getAnnouncemnFrom());
					preparedStatement.setString(7, "");

					preparedStatement.executeUpdate();
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
