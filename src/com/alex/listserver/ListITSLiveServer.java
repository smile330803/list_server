package com.alex.listserver;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListITSLiveServer implements Runnable {

	private String host;
	private int port;
	private Socket socket;

	public static void main(String args[]) {
		// scan /etc/ts/its_testservers to get all of the ITS test ServerName
		List<String> list = new ArrayList<String>();
		try {
			list = ListITSLiveServer.ListServer();

		} catch (Exception e) {
			e.printStackTrace();
		}
		// check if the ServerName in its_testservers is active, if yes, then
		// print it out
		StringBuffer sb = new StringBuffer();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			String a = (String) iterator.next();
			Socket s = openSocket(a, 8080, 20);
			if (s != null) {
				try {
					// ListITSLiveServer.addServers(a);
					sb.append(a).append("\n");
					System.out.println((new StringBuilder(String.valueOf(a)))
							.toString());
				} catch (Exception e) {
					System.out.println("Exception : " + a);
					e.printStackTrace();
				}
			}
		}
		System.exit(0);
	}

	public static Socket openSocket(String aHost, int aPort, int timeout) {
		ListITSLiveServer opener = new ListITSLiveServer(aHost, aPort);
		Thread t = new Thread(opener);
		t.start();
		try {
			t.join(timeout);
		} catch (InterruptedException interruptedexception) {
		}
		return opener.getSocket();
	}

	public ListITSLiveServer(String aHost, int aPort) {
		socket = null;
		host = aHost;
		port = aPort;
	}

	public void run() {
		try {
			socket = new Socket(host, port);
		} catch (IOException ioexception) {
		}
	}

	public Socket getSocket() {
		return socket;
	}

	// get the name of the test server in its_testservers
	public static List<String> ListServer() throws Exception {
		String path = "/etc/ts/its_testservers";
		File f = new File(path);
		// if(!f.exists()){
		// f.createNewFile();
		// }
		BufferedReader in = new BufferedReader(new FileReader(path));
		String line;
		List<String> servers = new ArrayList<String>();

		while ((line = in.readLine()) != null) {
			servers.add(line);
		}
		return servers;
	}

}