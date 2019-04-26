package no.hvl.dat110.aciotdevice.client;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class RestClient {

	public RestClient() {
		// TODO Auto-generated constructor stub
	}

	private static final String host = "localhost";
	private static final int port = 8080;

	private static String logpath = "/accessdevice/log";

	public void doPostAccessEntry(String message) {

		// TODO: implement a HTTP POST on the service to post the message

		try (Socket s = new Socket(host, port)){

			Gson gson = new Gson();
			String jsonbody = gson.toJson(new AccessMessage(message));

			String httpPutRequest = "POST " + logpath + " HTTP/1.1\r\n" +
					"Host: " + host + "\r\n" +
					"Content-type: application/json\r\n" +
					"Content-length: " + jsonbody.length() + "\r\n" +
					"Connection: close\r\n" + "\r\n" +
					jsonbody + "\r\n";

			OutputStream out = s.getOutputStream();

			PrintWriter print = new PrintWriter(out, false);
			print.print(httpPutRequest);
			print.flush();

			InputStream in = s.getInputStream();

			Scanner scan = new Scanner(in);
			StringBuilder jsonresponse = new StringBuilder();
			boolean header = true;

			while (scan.hasNext()){
				String nextLine = scan.nextLine();

				if (header){
					System.out.println(nextLine);
				}else {
					jsonresponse.append(nextLine);
				}

				if (nextLine.isEmpty()){
					header = false;
				}
			}
			System.out.println("Body:");
			System.out.println(jsonresponse.toString());

			scan.close();


		}catch (IOException ex){
			System.err.println(ex);
		}
		
	}
	
	private static String codepath = "/accessdevice/code";
	
	public AccessCode doGetAccessCode() {

		AccessCode code = null;
		
		// TODO: implement a HTTP GET on the service to get current access code

		try (Socket s = new Socket(host, port)){
			String httpGetRequest = "GET " + codepath + "HTTP/1.1\r\n" +
					"Accept: application/json\r\n" +
					"Host: localhost\r\n" +
					"Connection: close\r\n" + "\r\n";

			OutputStream out = s.getOutputStream();

			PrintWriter print = new PrintWriter(out, false);
			print.print(httpGetRequest);
			print.flush();

			InputStream in = s.getInputStream();

			Scanner scan = new Scanner(in);
			StringBuilder jsonresponse = new StringBuilder();
			boolean header = true;

			while (scan.hasNext()){
				String nextLine = scan.nextLine();

				if (!header){
					jsonresponse.append(nextLine);
				}

				if (nextLine.isEmpty()){
					header = false;
				}
			}

			Gson gson = new Gson();

			code = gson.fromJson(jsonresponse.toString(), AccessCode.class);

			scan.close();

		}catch (IOException ex){
			System.err.println(ex);
		}

		
		return code;
	}
}
