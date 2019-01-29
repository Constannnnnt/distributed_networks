import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class tcpClientHang {
    private Socket socket;

    private tcpClientHang(InetAddress serverAddress, int serverPort) throws Exception {
        this.socket = new Socket(serverAddress, serverPort);
    }

    public static float[] doInsertionSort(float[] input) {
        float temp;
        for (int i = 1; i < input.length; i++) {
            for (int j = i; j > 0; j--) {
                if (input[j] < input[j - 1]) {
                    temp = input[j];
                    input[j] = input[j - 1];
                    input[j - 1] = temp;
                }
            }
        }
        return input;

    }

    private void connect() throws Exception{

        try {
            byte[] data = {0x01};

            PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            toServer.println(new String(data));
            String ack = fromServer.readLine();
            System.out.println("FROM SERVER: " + ack);

            fromServer.close();
            socket.close();
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        float[] time = new float[100];
        float totaltime = 0;
        tcpClientHang client = new tcpClientHang(InetAddress.getByName(args[0]), Integer.parseInt(args[1]));

        System.out.println("\r\nConnected to Server: " + client.socket.getInetAddress());
        for (int i = 0; i < 100; i++) {

            long start = System.nanoTime();

            client.connect();

            long end = System.nanoTime();
            float duration = end - start;
            time[i] = duration / 1000000000;
            totaltime += duration / 1000000000;

        }

        System.out.println("Average Time: " + (totaltime / 100) + "seconds");
        float[] sortedTime = doInsertionSort(time);
        System.out.println("10th Percentile: " + sortedTime[9] + "seconds");
        System.out.println("90th Percentile: " + sortedTime[89] + "seconds");
    }
}