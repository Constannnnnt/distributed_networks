import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class tcpClient {
    private Socket socket;

    private tcpClient(InetAddress serverAddress, int serverPort) throws Exception {
        this.socket = new Socket(serverAddress, serverPort);
    }

    public float[] doInsertionSort(float[] input) {
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
        // byte[] data = {(byte) 0x01};
        // // byte[] data = new byte[10 * 1024 * 1024];
        // // for (int i  = 0; i < 10240; i++) {
        // //     data[i] = 0x01;
        // // }
        // float[] time = new float[100];
        // float totaltime = 0;
        // DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        // BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // for (int i = 0; i < 100; i++) {
        //     long start = System.nanoTime();

        //     out.write(data);
        //     String ack = in.readLine();
        //     System.out.println("FROM SERVER: " + ack);

        //     long end = System.nanoTime();
        //     float duration = end - start;
        //     time[i] = duration / 1000000000;
        //     totaltime += duration / 1000000000;
        // }
        // System.out.println("Average Time: " + (totaltime / 100) + "seconds");
        // float[] sortedTime = doInsertionSort(time);
        // System.out.println("10th Percentile: " + sortedTime[9] + "seconds");
        // System.out.println("90th Percentile: " + sortedTime[89] + "seconds");
        try {
            byte[] data = {(byte) 0x01};
            // byte[] data = new byte[10 * 1024 * 1024];
            // for (int i = 0; i < 10240; i++) {
            // data[i] = 0x01;
            // }
            float[] time = new float[100];
            float totaltime = 0;

            PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            for (int i = 0; i < 100; i++) {

                long start = System.nanoTime();

                toServer.println(new String(data));
                String ack = fromServer.readLine();
                System.out.println("FROM SERVER: " + ack);

                long end = System.nanoTime();
                float duration = end - start;
                time[i] = duration / 1000000000;
                totaltime += duration / 1000000000;

            }
            System.out.println("Average Time: " + (totaltime / 100) + "seconds");
            float[] sortedTime = doInsertionSort(time);
            System.out.println("10th Percentile: " + sortedTime[9] + "seconds");
            System.out.println("90th Percentile: " + sortedTime[89] + "seconds");
            toServer.close();
            fromServer.close();
            socket.close();
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        tcpClient client = new tcpClient(InetAddress.getByName(args[0]), Integer.parseInt(args[1]));

        System.out.println("\r\nConnected to Server: " + client.socket.getInetAddress());
        client.connect();
    }
}