package net.thumbtack.school.concert.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.JsonParser;

import net.thumbtack.school.database.jdbc.JdbcUtils;

/**
 * В базе данных MySQL создать список некоторых городов - столиц стран. Написать
 * метод, которые загружает этот список, для каждого города выводит страну и
 * национальную валюту используя сервис http://restcountries.eu (например
 * http://restcountries.eu/rest/v2/capital/london ). Написать тесты для этого
 * метода.
 */

public class CountryServer {

    public void startCountryServer() throws SQLException, IOException {
        for (int i = 0; i < 3; i++) {
            String capital = getCapitalById(i);
            String json = downloadJson(capital);
            String countryName = getStringValueByJsonName(json, "name");
            String currencies = getStringValueByJsonName(json, "currencies");
            String currenciesName = getStringValueByJsonName(currencies, "name");
            insertData(countryName, capital, countryName);
        }
    }

    /**
     * Download JSON for country
     *
     * @param countName
     * @return
     * @throws IOException
     */
    public String downloadJson(String countName) throws IOException {
        URL url = new URL("http://restcountries.eu/rest/v2/capital/" + countName);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        try (InputStreamReader in = new InputStreamReader((InputStream) conn.getContent());
             BufferedReader buff = new BufferedReader(in)) {
            return buff.readLine();
        }
    }

    /**
     * Parse JSON and get value string by name
     *
     * @param json
     * @param name
     * @return
     */
    public String getStringValueByJsonName(String json, String name) {
        return new JsonParser().parse(json).getAsJsonArray().get(0).getAsJsonObject().get(name).toString();
    }


    public static void insertData(String country, String capital, String currency) throws SQLException {
        String insertQuery = "INSERT INTO `country` values(?,?,?,?);";
        Connection con = JdbcUtils.getConnection();
        try (PreparedStatement stmt = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setNull(1, java.sql.Types.INTEGER);
            stmt.setString(2, capital);
            stmt.setString(3, country);
            stmt.setString(4, currency);
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            }
        }
    }

    public static String getCapitalById(int id) throws SQLException {
        String query = "SELECT capital FROM capital WHERE id=" + id + ";";
        Connection con = JdbcUtils.getConnection();
        String capital = new String();
        try (PreparedStatement stmt = con.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                capital = rs.getString("capital");
            }
        }
        return capital;
    }

    public static void deleteData() throws SQLException {
        String query = "DELETE FROM country";
        Connection con = JdbcUtils.getConnection();
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.executeUpdate();
        }
    }

}
