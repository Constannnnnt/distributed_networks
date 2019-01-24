import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStreamReader;

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
        Socket client = this.server.accept();
        String clientAddress = client.getInetAddress().getHostAddress();
        System.out.println("\r\nNew connection from " + clientAddress);

        DataInputStream in = new DataInputStream(client.getInputStream()));
        DataOutputStream out = new DataOutputStream(client.getOutputStream());

        int len = in.readInt();
        byte[] data = new byte[len];
        if (len > 0) {
            in.readFully(data);
        }

        if (data != null) {
            System.out.println("\r\nMessage from " + clientAddress + ": " + data);
        }

        String ack = "ack";
        out.writeBytes(ack);
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