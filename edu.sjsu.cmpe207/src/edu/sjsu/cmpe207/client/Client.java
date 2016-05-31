package edu.sjsu.cmpe207.client;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.sjsu.cmpe207.fileSplitter.SplitFile;

public class Client {
	
    Socket clientSocket;
	
	Client(Socket clientSocket) {
		
		this.clientSocket = clientSocket;
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		
		while(true) {
			
			//Socket ccSocket = new Socket("localhost",1234);
			
			new Thread().start();
		}
		
	}
	
	public void run() {
	    try {
	    	
	    	String f = null;
			sendFile(f);
	      
	      clientSocket.close();
	    } catch (IOException e) {
	      System.out.println(e);
	    }
	  }
	
	public static void sendFile(String f) throws IOException {
		
        String file = f;
	    
	   String filePath = "/home/vikas/t/" + file;
        
       // Scanner console = new Scanner(System.in);
       // System.out.println("enter the file path where files will be stored after creation: ");
       // String filePath = console.next()+file;
        
        
	    File transferFile = new File (filePath);
	    
	    //splitFile(filePath);
	    
	    ArrayList<File> listPartFile = splitFile(filePath);
	    
	    //fileTransferServer(transferFile, file, splitFile(filePath));
	    
	    for(int i =0;i<listPartFile.size();i++)
	    {
	    	float eof = 0;
	    	File temp = listPartFile.get(i);
	    	if(i == listPartFile.size()-1)
	    		eof = 1;
	    	fileTransferServer(temp,eof, file);
	    }
		
	}

	private static ArrayList<File> splitFile(String file) throws IOException {
		
		//int PART_SIZE = 5000000;
		int PART_SIZE = 20;
		
		
		File inputFile = new File(file);
		FileInputStream inputStream;
		String newFileName;
		FileOutputStream filePart;
		DataInputStream dis = null;
		
		int fileSize = (int) inputFile.length();
		int nChunks = 0, read = 0, readLength = PART_SIZE;
		
		ArrayList<File> list = new ArrayList<File>();
		System.out.println(inputFile);
		inputStream = new FileInputStream(inputFile);
		dis = new DataInputStream(inputStream);
		int startAddr = 0;
		
			while (fileSize > 0) {
				if (fileSize <= PART_SIZE) {
					readLength = fileSize;
				}
				
				byte[] byteChunkPart = new byte[readLength];
				
				read = inputStream.read(byteChunkPart);
				System.out.println(read);
				fileSize -= read;
				startAddr += readLength;
				assert (read == byteChunkPart.length);
				nChunks++;
				
				newFileName = file.replace("/home/vikas/t/", "/home/vikas/tmp/");
				newFileName = newFileName + ".part"
						+ Integer.toString(nChunks - 1);
			
				
				File myFile = new File(newFileName);
				
				//creating an array of all the files names created by splitting the file
				
				
				list.add(myFile);
				
				filePart = new FileOutputStream(new File(newFileName));
				filePart.write(byteChunkPart);
				filePart.flush();
				filePart.close();
				byteChunkPart = null;
				filePart = null;
				
			}
			System.out.println(list);
			
		return list;
		
	}
	
//	public static void fileTransferServer(File transferFile, String file, ArrayList<File> list) throws UnknownHostException, IOException {
	public static void fileTransferServer(File fis2,float eof, String file) throws UnknownHostException, IOException {		
		Socket cs = new Socket("localhost",1234);
		
		//for(File fis2 : list) {
		/*for(int i = 0;i<list.size();i++)
		{
			File fis2 = list.get(i);
			*/
			FileInputStream fis1 = new FileInputStream(fis2);
			
			// get the size of the file and store it in byte array
			byte [] bytearray2 = new byte [(int) fis2.length()];
			
			BufferedInputStream bin2 = new BufferedInputStream(fis1);
			
			// Read the input stream data
			
			DataInputStream dis = new DataInputStream(bin2);
			//dis.readFully(bytearray2, 0, bytearray2.length);
			dis.read(bytearray2);
			
			// handle the file and send it over the socket
			
			OutputStream os2 = cs.getOutputStream();
			
			//sending the file name and file size over the socket
			
			DataOutputStream dos = new DataOutputStream(os2);
			System.out.println(fis2.getName());
			dos.writeUTF(fis2.getName());
			dos.writeUTF(file);
			dos.writeLong(bytearray2.length);
			/*if(i == list.size()-1)
			{
				//send flag = 1;
				dos.writeFloat(1);
			}
			else
			{
				//send flag = 0;
				dos.writeFloat(0);
			}*/
			dos.writeFloat(eof);
			
			dos.write(bytearray2);
			//dos.write(bytearray2, 0, bytearray2.length);
			dos.flush();
			
			System.out.println("sending files to the server: "+ fis2.getName());
			
		//}
		cs.close();
		//delete all partition files
		/*
		
		FileInputStream fis = new FileInputStream(transferFile);
		 // get the size of the file and store it in byte array
		    byte [] bytearray1  = new byte [(int)transferFile.length()]; 
		    BufferedInputStream bin1 = new BufferedInputStream(fis);
		    // Read the input stream data
		    DataInputStream dis = new DataInputStream(bin1);
		    dis.readFully(bytearray1, 0, bytearray1.length);
		    
		  //  bin1.read(bytearray1, 0, bytearray1.length);
		    
		    //handle the file and send it over the socket
		    OutputStream os1 = cs.getOutputStream();
		    
		    //sending the file name and file size over the socket
		    
		    
		    
		    DataOutputStream dos = new DataOutputStream(os1);
		    dos.writeUTF(transferFile.getName());
		    dos.writeLong(bytearray1.length);
		    dos.write(bytearray1, 0, bytearray1.length);	    
		    dos.flush();
		    
		    System.out.println("sending files to the server: " + file);
		    
		   // os1.write(bytearray1, 0, bytearray1.length);
		  //  os1.flush();
		   // bin1.close();
		    
		    cs.close();
		    
		    */
		
	}
}
