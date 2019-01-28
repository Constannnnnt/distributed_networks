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
        byte[] data = {(byte) 0x01};
        float[] time = new float[100];
        float totaltime = 0;
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        for (int i = 0; i < 100; i++) {
            long start = System.nanoTime();

            out.write(data);
            String ack = in.readLine();
            System.out.println("FROM SERVER: " + ack);

            long end = System.nanoTime();
            float duration = end - start;
            time[i] = duration / 1000000000;
            totaltime += duration / 1000000000;
        }
        System.out.println("Average Time: " + (totaltime / 100) + "seconds");
        float[] sortedTime = doInsertionSort(time);
        System.out.println("10th Percentile: " + Float.toString(sortedTime[9]) + "seconds");
        System.out.println("90th Percentile: " + Float.toString(sortedTime[89]) + "seconds");

        socket.close();
    }

    public static void main(String[] args) throws Exception {
        tcpClient client = new tcpClient(InetAddress.getByName(args[0]), Integer.parseInt(args[1]));

        System.out.println("\r\nConnected to Server: " + client.socket.getInetAddress());
        client.connect();
    }
}