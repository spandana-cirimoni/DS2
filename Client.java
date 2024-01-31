import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

public class Client {
	private static final int Server_PORT=9999;
	private static final String Server_IP="in-csci-rrpc01.cs.iupui.edu";
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket=new Socket(Server_IP,Server_PORT);
		
		BufferedReader bf=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		BufferedReader userInput=new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out= new PrintWriter(socket.getOutputStream(),true);
		
		System.out.println("[CLIENT] Enter the username and password:");
		System.out.print("[CLIENT] Username:");
		String username=userInput.readLine();
		out.println(username);
		System.out.print("[CLIENT] Password:");
		String password=userInput.readLine();
		out.println(password);
		
		String serverResponse= bf.readLine();
		JOptionPane.showMessageDialog(null, serverResponse);
		
		String flag=bf.readLine();
		if(flag.equalsIgnoreCase("1") ) {
			System.out.print("[CLIENT] Enter the FileName(and filetype):");
			String fileName=userInput.readLine();
			out.println(fileName);
			String s=bf.readLine();
			if(s.equalsIgnoreCase("True")) {
				out.println("OK");
				byte b[]=new byte[4*1024];
				InputStream is=socket.getInputStream();
				String outputFilePath=".\\"+getRandomFileName()+".txt";
				FileOutputStream fos=new FileOutputStream(outputFilePath);
				is.read(b,0,b.length);
				String content=new String(b);
				//System.out.println(content);
				String DecryptedData=Decryption(content);
				//System.out.println(DecryptedData);
				byte[] buffer = DecryptedData.getBytes();
				fos.write(buffer,0,buffer.length);
				System.out.print("[CLIENT] Received the file successfully!");
				fos.close();
			}
		}
		socket.close();
		System.exit(0);
	}
	public static String getRandomFileName()
    {
        int n=(int) ((Math.random() * (200 - 100)) + 100);
        return "Output"+n;
    }
	public static String Decryption(String content) throws IOException
    {
	    int key = 4;
	    String Decrypted="";
	    for(int i=0;i<content.length();i++){
	    	int c=content.charAt(i);
	    	if(Character.isUpperCase(c)) {
	    		c=c-(key%26);
	    		if(c<'A') {
	    			c=c+26;
	    		}
	    	}
	    	else if(Character.isLowerCase(c)) {
		    	c=c-(key%26);
		    	if(c<'a') {
		    		c=c+26;
		    	}
	    	}
	    	Decrypted+=(char)c;
	    }
	    	return Decrypted;
	 }
}
