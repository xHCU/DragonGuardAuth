package org.dragonguard.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.dragonguard.server.utils.EncryptionUtil;
import org.dragonguard.server.utils.LogUtil;
import org.dragonguard.server.utils.ColorUtil;
import org.json.JSONObject;

///////////////DRAGON GUARD AUTH SERVER VERSION: ALPHA///////////////
////////////////////BY HACKEDCLIENT CRACKER UNION////////////////////
public class Main {

	private ServerSocket serverSocket;
	private EncryptionUtil aes = new EncryptionUtil();

	public static void main(String[] args) {
		new Main().start();
	}
	
	public void start() {
		try {
			serverSocket = new ServerSocket(1337);
			serverSocket.setSoTimeout(200);
		} catch (IOException e) {
			e.printStackTrace();
			LogUtil.printError("Start authentication server failed.");
			System.exit(0);
		}
		LogUtil.printInfo("Authentication server started.");
		run();
	}

	public void run() {
		while (true) {
			try {
				Socket server = serverSocket.accept();
				Thread thread = new Thread(() -> {
					handleConnection(server);
				});
				thread.start();
			} catch (SocketTimeoutException s) {
			} catch (IOException e) {
				LogUtil.printError(e.toString());
			}
		}
	}
	
	public void handleConnection(Socket server) {
		try {
			int stage = 1;
			SocketAddress socketAddress = server.getRemoteSocketAddress();
			InetAddress inetAddress = ((InetSocketAddress)socketAddress).getAddress();
			BufferedReader input = new BufferedReader(new InputStreamReader(server.getInputStream()));
			DataOutputStream output = new DataOutputStream(server.getOutputStream());
			LogUtil.printInfo("%s connected.", inetAddress.getHostAddress().toString());
			String string = "";
			while ((string = input.readLine()) != null) {
				String firstKey = Keys.aes1;
				String secondKey = Keys.aes2 + inetAddress.getHostAddress().toString();
				JSONObject json = new JSONObject(string);
				String message = json.getString("message");
				if (json.getInt("stage") == 1 && aes.decrypt(message, firstKey).equals("getIp")) {
					JSONObject json2 = new JSONObject();
					json2.put("secret", aes.encrypt(inetAddress.getHostAddress().toString(), firstKey));
					json2.put("key", aes.encrypt(Keys.aes3, firstKey));
					output.writeUTF(json2.toString());
					stage++;
				}
				if (json.getInt("stage") == 2) {
					if (stage == 2) {
						output.writeUTF(getUID(aes.decrypt(message, secondKey), secondKey, inetAddress));
						stage++;
					} else {
						LogUtil.printError("%s%s%s tried skip [1/3] stage.", ColorUtil.WHITE, inetAddress.getHostAddress().toString(), ColorUtil.RED);
						JSONObject json2 = new JSONObject().put("error", "3");
						output.writeUTF(json2.toString());
						server.close();
					}
				}
				if (json.getInt("stage") == 3) {
					if (stage == 3) {
						secondKey += Keys.aes5;
						String hwid = aes.decrypt(json.getString("message"), secondKey); 
						String uid = aes.decrypt(json.getString("key"), secondKey); 
						String thirddKey = Keys.aes6 + hwid + uid;
						JSONObject json2 = new JSONObject();
						if (isDataValid(hwid, uid)) {
							LogUtil.printInfo("%s logged in: HWID - %s, UID - %s.", inetAddress.getHostAddress().toString(), hwid, uid);
							json2.put("key", aes.encrypt(Keys.aes7, thirddKey));
							output.writeUTF(json2.toString());
						} else {
							LogUtil.printError("%s%s%s sent invalid data: %sHWID - %s%s, %sUID - %s.", ColorUtil.WHITE, inetAddress.getHostAddress().toString(), ColorUtil.RED, ColorUtil.WHITE, hwid, ColorUtil.RED, ColorUtil.WHITE, uid);
							json2.put("error", "2");
							output.writeUTF(json2.toString());
							server.close();
						}
					} else {
						LogUtil.printError("%s%s%s tried skip [2/3] stage.", ColorUtil.WHITE, inetAddress.getHostAddress().toString(), ColorUtil.RED);
						JSONObject json2 = new JSONObject().put("error", "3");
						output.writeUTF(json2.toString());
						server.close();
					}
				}
			}
		} catch (SocketTimeoutException s) {
		} catch (SocketException s) {
		} catch (Exception e) {
			LogUtil.printError(e.toString());
		}
	}
	
	public String getUID(String hwid, String key, InetAddress inetAddress) {
		//////////////Write your code here/////////////
		if (hwid.equals("a7a5c4d1f8ab9461ab4ca04ad47f86d9")) {
			JSONObject json = new JSONObject();
			//uid
			json.put("secret", aes.encrypt("10", key));
			json.put("key", aes.encrypt(Keys.aes4, key));
			return json.toString();
		}
		///////////////////////////////////////////////
		
		//return error if hwid not in the database
		LogUtil.printError("%s%s%s's hwid not in the database: %sHWID - %s.", ColorUtil.WHITE, inetAddress.getHostAddress().toString(), ColorUtil.RED, ColorUtil.WHITE, hwid);
		JSONObject json = new JSONObject().put("error", "1");
		return json.toString();
	}
	
	public boolean isDataValid(String hwid, String uid) {
		//////////////Write your code here/////////////
		if (hwid.equals("a7a5c4d1f8ab9461ab4ca04ad47f86d9") && uid.equals("10")) {
			return true;
		}
		///////////////////////////////////////////////
		return false;
	}
}
