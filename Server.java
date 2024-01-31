import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;

public class Server {
	private static final int PORT=9999;
	private static List<String> uname=new ArrayList<String>(Arrays.asList("spandana"));
	private static List<String> pass=new ArrayList<String>(Arrays.asList("cirimoni"));
	
	public static void main(String[] args) throws UnknownHostException, IOException, FileNotFoundException{
		ServerSocket ss=new ServerSocket(PORT);
		
		System.out.println("[SERVER] Waiting for Client Connection.....");
		Socket client=ss.accept();
		
		PrintWriter out= new PrintWriter(client.getOutputStream(),true);
		BufferedReader in=new BufferedReader(new InputStreamReader(client.getInputStream()));
		try{
			// Username and Password Authentication.
			String username=in.readLine();
			String password=in.readLine();
			int flag=0;
			if(uname.contains(username.toLowerCase())) {
				int index=uname.indexOf(username.toLowerCase());
				if(pass.get(index).equalsIgnoreCase(password)) {
					flag=1;
					out.println("[SERVER] Welcome,"+username+".");
					out.println("1");
					System.out.println("[SERVER] Connected to Client!");
				}
				else {
					out.println("[SERVER] The password is incorrect! Try again!");
					out.println("0");
				}
			}
			else {
				uname.add(username);
				pass.add(password);
				flag=1;
				out.println("[SERVER] You are registered and logged in!");
				out.println("1");
			}
			if(flag==1) {
				// Check whether the file exists or not
				String fileName=in.readLine();
				System.out.println("[SERVER] The FileName: "+fileName);
				File file=new File(fileName);
				if(file.exists()) {
					System.out.println("[SERVER] The File Exists!");
			        out.println("True");
			        String s=in.readLine();
			        System.out.println("[SERVER] "+s);
			        FileInputStream fis = new FileInputStream(file);
			        byte[] b = new byte[4*1024];
			        StringBuilder sb = new StringBuilder();
			        while (fis.read(b) != -1) {
			        	sb.append(new String(b));
			        	b = new byte[4*1024];
			        }
			        String content = sb.toString();
			        //System.out.println(content);
			        String EncryptedData=Encryption(content);
			        //System.out.println(EncryptedData);
			        FileWriter fw = new FileWriter(fileName);
				    fw.write(EncryptedData);
				    fw.close();
			        byte[] buffer = EncryptedData.getBytes();
			        OutputStream os=client.getOutputStream();
			        os.write(buffer, 0,buffer.length);
			        
			        fis.close();
			        os.flush();
			        System.out.println("[SERVER] File Sent to Client. Closing!");
				}
				else {
					System.out.println("[SERVER] The File does not exist!");
					out.println("False");
				}
			}
		}finally {
			out.close();
			in.close();
		}
		client.close();
		ss.close();
	}
	public static String Encryption(String content) throws IOException{
	    int key = 4;
	    String encrypted="";
	    for(int i=0;i<content.length();i++){
	    	int c=content.charAt(i);
	    	if(Character.isUpperCase(c)) {
	    		c=c+(key%26);
	    		if(c>'Z') {
	    			c=c-26;
	    		}
	    	}
	    	else if(Character.isLowerCase(c)) {
		    	c=c+(key%26);
		    	if(c>'z') {
		    		c=c-26;
		    	}
	    	}
	    	encrypted+=(char)c;
	    }
	    	return encrypted;
	  }
}
