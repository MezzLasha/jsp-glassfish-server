package com.exam.assignment;

import java.io.*;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <title>Car Information Form</title>\n" +
                "    <style>\n" +
                "      body {\n" +
                "        display: flex;\n" +
                "        justify-content: center;\n" +
                "        align-items: center;\n" +
                "        height: 100vh;\n" +
                "        background-color: #f8f8f8;\n" +
                "      }\n" +
                "      form {\n" +
                "        display: flex;\n" +
                "        flex-direction: column;\n" +
                "        align-items: center;\n" +
                "        justify-content: center;\n" +
                "        background-color: white;\n" +
                "        border-radius: 10px;\n" +
                "        padding: 30px;\n" +
                "        width: 400px;\n" +
                "        box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1);\n" +
                "      }\n" +
                "      input[type=\"text\"],\n" +
                "      input[type=\"number\"] {\n" +
                "        width: 100%;\n" +
                "        padding: 10px;\n" +
                "        margin-bottom: 15px;\n" +
                "        border: none;\n" +
                "        border-radius: 5px;\n" +
                "        box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1);\n" +
                "      }\n" +
                "      input[type=\"submit\"] {\n" +
                "        background-color: #4CAF50;\n" +
                "        color: white;\n" +
                "        border: none;\n" +
                "        border-radius: 5px;\n" +
                "        padding: 15px;\n" +
                "        width: 100%;\n" +
                "        cursor: pointer;\n" +
                "        font-size: 18px;\n" +
                "        margin-top: 20px;\n" +
                "      table {\n" +
                "        font-family: arial, sans-serif;\n" +
                "        border-collapse: collapse;\n" +
                "        width: 100%;\n" +
                "      }\n" +
                "      th, td {\n" +
                "        border: 1px solid #dddddd;\n" +
                "        text-align: left;\n" +
                "        padding: 8px;\n" +
                "      }\n" +
                "      tr:nth-child(even) {\n" +
                "        background-color: #dddddd;\n" +
                "      }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "      <form action=\"hello-servlet\" method=\"post\">\n" +
                "      <h1 style=\"text-align: center;\">Car Information Form</h1>\n" +
                "      <input type=\"text\" id=\"make\" name=\"make\" placeholder=\"Car Make\">\n" +
                "      <input type=\"text\" id=\"model\" name=\"model\" placeholder=\"Car Model\">\n" +
                "      <input type=\"number\" id=\"year\" name=\"year\" placeholder=\"Year\">\n" +
                "      <input type=\"text\" id=\"color\" name=\"color\" placeholder=\"Color\">\n" +
                "      <input type=\"number\" id=\"miles\" name=\"miles\" placeholder=\"Mileage\">\n" +
                "      <input type=\"submit\" value=\"Submit\">\n" +
                "    </form>\n" +
                "      <h1 style=\"text-align: center;\">Cars Table</h1>\n");

        // Retrieve the rows from the cars table
        try {
            // Load the MySQL JDBC driver
            log("e.getMessage()");
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a database connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/myschema", "Lasha", "mypassword");

            // Create the SQL query
            String query = "SELECT * FROM cars";
            PreparedStatement stmt = conn.prepareStatement(query);

            // Execute the SQL query
            ResultSet rs = stmt.executeQuery();

            // Create the HTML table
            out.println("<table>");
            out.println("<tr>");
            out.println("<th>ID</th>");
            out.println("<th>Make</th>");
            out.println("<th>Model</th>");
            out.println("<th>Year</th>");
            out.println("<th>Color</th>");
            out.println("<th>Mileage</th>");
            out.println("</tr>");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("id") + "</td>");
                out.println("<td>" + rs.getString("make") + "</td>");
                out.println("<td>" + rs.getString("model") + "</td>");
                out.println("<td>" + rs.getInt("year") + "</td>");
                out.println("<td>" + rs.getString("color") + "</td>");
                out.println("<td>" + rs.getInt("mileage") + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");

            // Close the database connection
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            log(e.getMessage());
        }

        out.println("</body>\n" +
                "</html>\n");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        log("test");

        // Retrieve the form data
        String make = req.getParameter("make");
        String model = req.getParameter("model");
        int year = Integer.parseInt(req.getParameter("year"));
        String color = req.getParameter("color");
        int miles = Integer.parseInt(req.getParameter("miles"));


        // Insert the data into the database
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a database connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "Lasha", "mypassword");

            // Create the SQL query
            String query = "INSERT INTO myschema.cars (make, model, year, color, mileage) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, make);
            stmt.setString(2, model);
            stmt.setInt(3, year);
            stmt.setString(4, color);
            stmt.setInt(5, miles);

            // Execute the SQL query
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Data inserted successfully!");
            } else {
                System.out.println("Data insertion failed!");
            }

            // Close the database connection
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        resp.sendRedirect("hello-servlet");

    }

    public void destroy() {
    }
}