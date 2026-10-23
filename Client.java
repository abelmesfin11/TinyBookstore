import java.net.URL;
import java.util.Map;
import java.util.Scanner;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;


public class Client {

    public static void main(String[] args) throws Exception {
        // setup: point a client at the server (once)
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("http://localhost:8888"));

        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        // one remote call
        // Object result = client.execute("bookstore.lookup", new Object[]{53477});
        // Map<?, ?> book = (Map<?, ?>) result;
        // System.out.println(book.get("title") + "  $" + book.get("cost")
        //         + "  stock: " + book.get("stock"));


        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String line = in.nextLine();
            String[] parts = line.trim().split("\\s+");

            switch (parts[0]) {
                case "search":
                    String topic = String.join(" ", java.util.Arrays.copyOfRange(parts, 1, parts.length));
                    Object[] results = (Object[]) client.execute("bookstore.search", new Object[]{topic});
                    for (Object r : results) {
                        Map<?, ?> b = (Map<?, ?>) r;
                        System.out.println(b.get("id") + "  " + b.get("title"));
                    }
                    break;

                case "lookup":
                    Object result = client.execute("bookstore.lookup", new Object[]{Integer.parseInt(parts[1])});
                    Map<?, ?> book = (Map<?, ?>) result;
                    if (book.isEmpty()) {
                        System.out.println("no such item: " + parts[1]);
                    } else {
                        System.out.println(book.get("title") + "  $" + book.get("cost")
                                + "  topic: " + book.get("topic") + "  stock: " + book.get("stock"));
                    }
                    break;

                case "buy":
                    int bought = (Integer) client.execute("bookstore.buy", new Object[]{Integer.parseInt(parts[1])});
                    System.out.println(bought == -1 ? "out of stock" : "bought " + bought);
                    break;

                default:
                    System.out.println("commands: search <topic> | lookup <id> | buy <id>");
            }
        }

    }
}
