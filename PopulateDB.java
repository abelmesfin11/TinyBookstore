import java.sql.*;

public class PopulateDB {

   public static void main( String args[] ) {
      Connection c = null;
      Statement stmt = null;

      try {
         Class.forName("org.sqlite.JDBC");
         c = DriverManager.getConnection("jdbc:sqlite:books.db");
         c.setAutoCommit(false);
         System.out.println("Opened database successfully");

         stmt = c.createStatement();

         // Start fresh each run so the populate script is repeatable
         stmt.executeUpdate("DROP TABLE IF EXISTS BOOKS");

         String sql = "CREATE TABLE BOOKS " +
                        "(ID INT PRIMARY KEY          NOT NULL, " +
                        " TITLE          CHAR(100)    NOT NULL, " +
                        " TOPIC          CHAR(50)     NOT NULL, " +
                        " STOCK          INT          NOT NULL, " +
                        " PRICE          FLOAT        NOT NULL)";
         stmt.executeUpdate(sql);

         sql = "INSERT INTO BOOKS (ID, TITLE, TOPIC, STOCK, PRICE) VALUES " +
               "(53477, 'Achieve Less Bugs and More Hugs in CSCI 339', 'distributed systems', 10, 49.99), " +
               "(53573, 'Distributed Systems for Dummies',             'distributed systems', 10, 39.99), " +
               "(12365, 'Surviving College',                           'college life',        10, 24.99), " +
               "(12498, 'Cooking for the Impatient Undergraduate',     'college life',        10, 19.99)";
         stmt.executeUpdate(sql);

         stmt.close();
         c.commit();
         c.close();
      } catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(1);
      }
      System.out.println("Records created successfully");
   }
}
