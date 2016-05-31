package edu.sjsu.cmpe207.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server implements Runnable {
	  Socket csocket;

	  ArrayList<File> list = new ArrayList<File>();
	  
	  Server(Socket csocket) {
	    this.csocket = csocket;
	  }

	  public static void main(String args[]) throws Exception {
		  

		  ServerSocket ssock = new ServerSocket(1234);
		  
		  while (true) {
		      Socket sock = ssock.accept();
		      //System.out.println("Connected");
		      new Thread(new Server(sock)).start();
		     // System.out.println("file received!!!");
		    }
	    
	  }
	  
	  public void run() {
		    try {
		    	
		      receiveFile();
		      csocket.close();
		    } catch (IOException e) {
		      System.out.println(e);
		    }
		  }
	  
	  public void receiveFile() throws IOException {
		  
		  int bytesRead;
	      DataInputStream clientData;
		  clientData = new DataInputStream(csocket.getInputStream());  
	      String fileName = null;
	      String actualFileName = null;
	      
	      fileName = clientData.readUTF();
	      actualFileName = clientData.readUTF();
	      long size;
			size = clientData.readLong();
	      File file = new File(fileName);
	      Float flag = clientData.readFloat();
	      
	      if(flag == 0)
	      {
	    	  list.add(file);
	      }
	      else
	      {
	    	  list.add(file);
	    	  mergeFile(list, actualFileName);
	      }
	      
			System.out.println(fileName);
			System.out.println("actual file name is: " +actualFileName);
			
	      FileOutputStream fos = null;
			fos = new FileOutputStream(("/home/vikas/study/transfer/"+fileName));
	      
	      byte[] buffer = new byte[1024];
			while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
				  fos.write(buffer, 0, bytesRead);
				  //System.out.println("total bytes read so far: " + bytesRead);
				  size -= bytesRead;
				  //System.out.println("total bytes still left: " + size);
	  }
			fos.close();
	  
	  }

	private void mergeFile(ArrayList<File> list, String fileName) throws IOException {
		
	//	File path=new File("/home/vikas/study/transfer/");
		
		File path=new File("/home/vikas/study/transfer/");
		
	    File[] listOfFiles = path.listFiles();
	   
	    BufferedWriter bw=new BufferedWriter(new FileWriter("/home/vikas/study/tmp/"+fileName));
	   
	    String s=new String();
	    for (int i = 0; i < listOfFiles.length; i++) {
	        File file = listOfFiles[i];
	        if (file.isFile()) {
	            BufferedReader br=new BufferedReader(new FileReader(file));
	            while ((s=br.readLine())!=null){
	                bw.write(s);
	            }
	        }
	    }
	   bw.close();
		
		
	
}
}
