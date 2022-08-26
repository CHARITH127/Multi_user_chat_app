package model;

import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String userName;

    public Client(Socket socket ,String userName){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userName = userName;

        }catch (IOException e){
            closeEveryThing(socket,bufferedReader,bufferedWriter);
        }
    }

    public void sendMessage(String message){
        try {
            /*bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();*/

            String messageToSend = message; /*scanner.nextLine();*/
            bufferedWriter.write(userName+" : "+messageToSend);
            bufferedWriter.newLine();
            bufferedWriter.flush();


            /*Scanner scanner = new Scanner(System.in);*/

            /*while (socket.isConnected()) {
                String messageToSend = message; *//*scanner.nextLine();*//*
                bufferedWriter.write(userName+" : "+messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }*/

        }catch (IOException e){
            closeEveryThing(socket,bufferedReader,bufferedWriter);
        }
    }

    public void senImages(String imagePath){

    }


    /*public void listenForMessage(){

        new Thread(new Runnable() {
            @Override
            public void run() {

                String messageFromGroupChat ;

                while (socket.isConnected()) {

                    try {

                        messageFromGroupChat = bufferedReader.readLine();
                        System.out.println(messageFromGroupChat);

                    }catch (IOException e){
                        closeEveryThing(socket,bufferedReader,bufferedWriter);
                    }

                }

            }
        }).start();

    }*/

    public BufferedWriter getBufferedWriter(){
        return bufferedWriter;
    }

    public BufferedReader getBufferReader(){
        return bufferedReader;
    }

    public void closeEveryThing(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
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

    /*public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the user name : ");
        String userName = scanner.nextLine();
        Socket socket = new Socket("localhost",3000);
        model.Client client = new model.Client(socket,userName);
        client.listenForMessage();
        client.sendMessage();
    }*/

}
