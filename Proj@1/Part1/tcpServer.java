import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class tcpServer {

    private ServerSocket server;

    public tcpServer(String ipAddress) throws Exception {
        if (ipAddress != null && !ipAddress.isEmpty()) {
            this.server = new ServerSocket(0, 1, InetAddress.getByName(ipAddress));
        } else {
            this.server = new ServerSocket(0, 1, InetAddress.getLocalHost());
        }
    }

    private void listen() throws Exception {
        try {
            Socket client = server.accept();
            String clientAddress = client.getInetAddress().getHostAddress();
            System.out.println("\r\nNew connection from " + clientAddress);

            PrintWriter toClient = new PrintWriter(client.getOutputStream(), true);
            BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            for (int i = 0; i < 100; i++) {
                String data = fromClient.readLine();
                toClient.println("ack");
                toClient.flush();
            }
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InetAddress getSocketAddress() {
        return this.server.getInetAddress();
    }

    public int getPort() {
        return this.server.getLocalPort();
    }

    public static void main(String[] args) throws Exception {
        tcpServer app = null;
        if (args.length == 0) {
            app = new tcpServer(null);
        } else {
            app = new tcpServer(args[0]);
        }

        System.out.println(
                "\r\nRunning Server: " + "Host=" + app.getSocketAddress().getHostAddress() + " Port=" + app.getPort());
        app.listen();
    }
}
