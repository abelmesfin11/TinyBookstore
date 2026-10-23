import java.util.*;
import java.sql.*;

public class BookstoreHandler {

    private static final List<String> purchases = Collections.synchronizedList(new ArrayList<>());

    static List<String> getPurchases() {
        return new ArrayList<>(purchases);   // copy so callers can iterate safely
    }

    public Object[] search(String topic) {
        List<Map<String, Object>> results = new ArrayList<>();
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:books.db");
             PreparedStatement ps = c.prepareStatement("SELECT ID, TITLE FROM BOOKS WHERE TOPIC = ?")) {

            ps.setString(1, topic);
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                        Map<String, Object> book = new HashMap<>();
                        book.put("id", rs.getInt("ID"));
                        book.put("title", rs.getString("TITLE"));
                        results.add(book);
                }
            }
        } catch (Exception e) {
            System.err.println("search failed: " + e.getMessage());
        }
        return results.toArray();
    }


    public Map<String, Object> lookup(int id) {
        Map<String, Object> book = new HashMap<>();
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:books.db");
            PreparedStatement ps = c.prepareStatement("SELECT TITLE, PRICE, TOPIC, STOCK FROM BOOKS WHERE ID = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    book.put("title", rs.getString("TITLE"));
                    book.put("cost", rs.getDouble("PRICE"));
                    book.put("topic", rs.getString("TOPIC"));
                    book.put("stock", rs.getInt("STOCK"));
                }
            }
        } catch (Exception e) {
            System.err.println("lookup failed: " + e.getMessage());
        }
        return book;

    }

    public int buy(int id) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:books.db");
                PreparedStatement ps = c.prepareStatement("UPDATE BOOKS SET STOCK = STOCK - 1 WHERE ID = ? AND STOCK > 0")) {
                ps.setInt(1, id);

                int rowsChanged = ps.executeUpdate();
                if (rowsChanged == 1){
                    purchases.add(new java.util.Date() + "  bought item " + id);
                    return id;
                }

        } catch(Exception e) {
            System.err.println("Buy failed: " + e.getMessage());
        }

        return -1; // out of stock

    }
}

