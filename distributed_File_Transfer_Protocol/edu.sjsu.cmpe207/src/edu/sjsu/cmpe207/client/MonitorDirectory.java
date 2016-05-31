package edu.sjsu.cmpe207.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class MonitorDirectory {
	
	public static void monitor() throws IOException, InterruptedException {
		
		Path faxFolder = Paths.get("/home/vikas/t/");
		WatchService watchService = FileSystems.getDefault().newWatchService();
		faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
		
		Client client = null;
		
		
		boolean valid = true;
		do {
			WatchKey watchKey = watchService.take();

			for (WatchEvent event : watchKey.pollEvents()) {
				WatchEvent.Kind kind = event.kind();
				if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
					
				String fileName = event.context().toString();   // event.context().toString();
					System.out.println("File Created:" + fileName);
					client.sendFile(fileName);
				}
			}
			valid = watchKey.reset();

		} while (valid);
				
	}
	
	/*
	public static DataOutputStream splitFile(String fileName, int part_size) throws IOException {
		
		File inputFile = new File(fileName);
		
		DataInputStream dis;
		DataOutputStream dos = null;
		
		FileInputStream inputStream;
		String newFileName;
		FileOutputStream filePart = null;
		
		int fileSize = (int) inputFile.length();
		int nChunks = 0, read = 0, readLength = part_size;
		byte[] byteChunkPart;
		File[] f;
		
			inputStream = new FileInputStream(inputFile);
			
			dis = new DataInputStream(inputStream);
			
			while (fileSize > 0) {
				if (fileSize <= 5) {
					readLength = fileSize;
				}
				byteChunkPart = new byte[readLength];
			
				//read = inputStream.read(byteChunkPart, 0, readLength);
				
				//reading in dataInputStream
				read = dis.read(byteChunkPart, 0, readLength);
				
				fileSize -= read;
				assert (read == byteChunkPart.length);
				nChunks++;
				newFileName = fileName + ".part"
						+ Integer.toString(nChunks - 1);
				
				File newFile = new File(newFileName);
				filePart = new FileOutputStream(new File(newFileName));
				
				//DataOutput Stream object instatiates here
				
			    dos = new DataOutputStream(filePart);
				
			    dos.writeUTF(newFileName);
			    dos.writeLong(newFile.length());
			    dos.write(byteChunkPart, 0, readLength);
			    dos.flush();
				
				filePart.write(byteChunkPart);
				filePart.flush();
				filePart.close();
				byteChunkPart = null;
				filePart = null;
			}
			inputStream.close();
		
			return dos;
		
		
	}
	
	*/

	public static void main(String[] args) throws IOException, InterruptedException {

		monitor();

	}

}