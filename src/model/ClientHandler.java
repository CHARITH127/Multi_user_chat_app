package model;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUserName;

    public ClientHandler(Socket socket){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUserName = bufferedReader.readLine();
            clientHandlers.add(this);
            broadCastMessage("model.Server :" + clientUserName +" enter the chat");
        }catch (IOException e){
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }


    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()){
            try {
                messageFromClient = bufferedReader.readLine();
                broadCastMessage(messageFromClient);

            }catch (IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);
                break;
            }
        }
    }

    public void broadCastMessage(String messageToSend){

        for (ClientHandler clientHandler : clientHandlers) {

            try {
                /*clientHandler.bufferedWriter.write(messageToSend);
                clientHandler.bufferedWriter.newLine();
                clientHandler.bufferedWriter.flush();*/
                if (!clientHandler.clientUserName.equals(clientUserName)){
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }catch (IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);
            }

        }

    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadCastMessage("model.Server :" + clientUserName + "left The chat");
    }

    public void closeEverything(Socket socket ,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
        removeClientHandler();
        try {
            if (bufferedReader!=null) {
                bufferedReader.close();
            }

            if (bufferedWriter!=null) {
                bufferedWriter.close();
            }

            if (socket!=null) {
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
