import java.sql.*;
import  java.util.*;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.webserver.WebServer;

public class Server {

    public static void main(String[] args) throws Exception {

        WebServer webServer = new WebServer(8888);
        PropertyHandlerMapping phm = new PropertyHandlerMapping();
        phm.addHandler("bookstore", BookstoreHandler.class);
        webServer.getXmlRpcServer().setHandlerMapping(phm);
        webServer.start();
        System.out.println("Server listening on 8888...");

        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {                        // keep going until the terminal closes
            String line = in.nextLine();                  // WAITS here until you type something + Enter
            String[] parts = line.trim().split("\\s+");   // "update 53477 59.99" -> ["update","53477","59.99"]

            switch (parts[0]) {                           // parts[0] is the command word
                case "log":
                    for (String entry : BookstoreHandler.getPurchases())
                        System.out.println(entry);
                    break;
                case "restock":
                    restock(100);
                    break;
                case "update":                            // parts[1] = id, parts[2] = new price
                    updatePrice(Integer.parseInt(parts[1]), Double.parseDouble(parts[2]));
                    break;
                default:
                    System.out.println("commands: log | restock | update <id> <price>");
            }
        }
    }


    private static void restock(int amount) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:books.db");
            PreparedStatement ps = c.prepareStatement("UPDATE BOOKS SET STOCK = STOCK + ?")) {
            ps.setInt(1, amount);
            int rows = ps.executeUpdate();
            System.out.println("restocked " + rows + " books by " + amount);
        } catch (Exception e) {
            System.err.println("restock failed: " + e.getMessage());
        }
    }

    private static void updatePrice(int id, double price) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:books.db");
            PreparedStatement ps = c.prepareStatement("UPDATE BOOKS SET PRICE = ? WHERE ID = ?")) {
            ps.setDouble(1, price);
            ps.setInt(2, id);
            int rows = ps.executeUpdate();
            System.out.println(rows == 1 ? "price updated" : "no such item: " + id);
        } catch (Exception e) {
            System.err.println("update failed: " + e.getMessage());
        }
    }

}



