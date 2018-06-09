package com.amazonbyod.webservices;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonbyod.redshift.AwsRedshiftOperations;

/**
 * Servlet implementation class RedShiftWebServices
 */
public class RedShiftWebServices extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RedShiftWebServices() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		AwsRedshiftOperations redshift = new AwsRedshiftOperations();
		response.getWriter().append("Served at: ").append(request.getContextPath());
		PrintWriter out = response.getWriter();
		Connection conn=redshift.redShiftConnect();
		
		try {
			Statement stmt=conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from testing");
			while(rs.next()){
				out.println("Name: "+rs.getString("name"));
				System.out.println(rs.getString("name"));
			}
			
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			;
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
