import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class tcpClient {
    private Socket socket;

    private tcpClient(InetAddress serverAddress, int serverPort) throws Exception {
        this.socket = new Socket(serverAddress, serverPort);
    }

    private void connect() throws Exception{
        byte[] data = {127};

        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.write(data);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String ack = in.readLine();
        System.out.println("FROM SERVER: " + ack);

        socket.close();
    }

    public static void main(String[] args) throws Exception {
        tcpClient client = new tcpClient(InetAddress.getByName(args[0]), Integer.parseInt(args[1]));

        System.out.println("\r\nConnected to Server: " + client.socket.getInetAddress());
        client.connect();
    }
}