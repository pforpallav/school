/*
 * University of British Columbia
 * Department of Computer Science
 * CPSC317 - Internet Programming
 * Assignment 2
 * 
 * Author: Jonatan Schroeder
 * January 2013
 * 
 * This code may not be used without written consent of the authors, except for 
 * current and future projects and assignments of the CPSC317 course at UBC.
 */

package ubc.cs317.rtsp.client.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import ubc.cs317.rtsp.client.exception.RTSPException;
import ubc.cs317.rtsp.client.model.Frame;
import ubc.cs317.rtsp.client.model.Session;

/**
 * This class represents a connection with an RTSP server.
 */
public class RTSPConnection {

	private static final int BUFFER_LENGTH = 15000;
	private static final long MINIMUM_DELAY_READ_PACKETS_MS = 20;

	private Session session;
	private Timer rtpTimer;
	// RTP
	static DatagramPacket packet; // UDP packet received from the server
	static DatagramSocket RTPsocket; // socket to be used to send and receive
	// UDP packets
	static int RTP_RCV_PORT = 24400; // port where the client will receive the
	// RTP packets
	static byte[] buf; // buffer used to store data received from the server
	static int countpk = 0;

	// RTSP
	private Socket socket;

	DataInputStream inputStream;
	DataOutputStream outputStream;

	BufferedReader RTSPBufferedReader;
	BufferedWriter RTSPBufferedWriter;
	static int RTSPSeqNo = 0;
	int RTSPid = 0; // ID of the RTSP session (given by the RTSP Server)
	static String videoFileName;
	final static String CRLF = "\r\n";
	static int state; // RTSP state == INIT or READY or PLAYING
	final static int INIT = 0;
	final static int READY = 1;
	final static int PLAYING = 2;
	Timer timer; //timer used to receive data from the UDP socket

	static   int setupstage= 0;
	static int sizem;
	static int tc=1;
	static int dt=1;
	static String value="idle"; 
	// TODO Add additional fields, if necessary

	int lastReceivedSequenceNumber = -1;
	//	static Frame f;
	static RTPpacket rtpp;
	static TreeMap<Integer, RTPpacket> packetMap = new TreeMap<Integer, RTPpacket>();
	static TreeMap<Integer, RTPpacket> packetMap2 = new TreeMap<Integer, RTPpacket>();
	static ArrayList<RTPpacket> buffer1 = new ArrayList<RTPpacket>();
	static ArrayList<RTPpacket> buffer2 = new ArrayList<RTPpacket>();
	static Boolean fullB1 = false;
	static Boolean fullB2 = false;
	static int i = 0;
	static final long deplay = 40;
	static Timer playTimer = new Timer();
	static int threadCount = 0;
	static long startTime = 0;
	static long endTime = 0;
	static long elapsedPlayTime = 0;
	static long howMany25Seconds = 0;
	static long mean25SFrames = 0;
	static double alpha = 0.125;
	static double estimatedRTT = 0;
	static double mostRecentRTT = 0;
	static double meanRTT = 0;
	static double highestRTT = 0;
	static ArrayList<Double> RTT = new ArrayList<Double>();
	static boolean isPlaying = false;
	static RTPpacket lastPacket=null;

	/**
	 * Establishes a new connection with an RTSP server. No message is sent at
	 * this point, and no stream is set up.
	 * 
	 * @param session
	 *            The Session object to be used for connectivity with the UI.
	 * @param server
	 *            The hostname or IP address of the server.
	 * @param port
	 *            The TCP port number where the server is listening to.
	 * @throws RTSPException
	 *             If the connection couldn't be accepted, such as if the host
	 *             name or port number are invalid or there is no connectivity.
	 */
	public RTSPConnection(Session session, String server, int port)
			throws RTSPException {

		this.session = session;
		try {
			System.setOut(new PrintStream(new File("output-file.txt")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			InetAddress ServerIPAddr = InetAddress.getByName(server);
			socket = new Socket(ServerIPAddr, port);

			RTSPBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			RTSPBufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			state = INIT;
			System.out.println("New RTSP state: INIT");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// TODO
	}

	/**
	 * Sends a SETUP request to the server. This method is responsible for
	 * sending the SETUP request, receiving the response and retrieving the
	 * session identification to be used in future messages. It is also
	 * responsible for establishing an RTP datagram socket to be used for data
	 * transmission by the server. The datagram socket should be created with a
	 * random UDP port number, and the port number used in that connection has
	 * to be sent to the RTSP server for setup. This datagram socket should also
	 * be defined to timeout after 1 second if no packet is received.
	 * 
	 * @param videoName
	 *            The name of the video to be setup.
	 * @throws RTSPException
	 *             If there was an error sending or receiving the RTSP data, or
	 *             if the RTP socket could not be created, or if the server did
	 *             not return a successful response.
	 */
	public synchronized void setup(String videoName) throws RTSPException {
		RTSPConnection.videoFileName = videoName;
		System.out.println(RTSPConnection.videoFileName);
		// TODO
		try {

			try {
				// construct a new DatagramSocket to receive RTP packets from
				// the server, on port RTP_RCV_PORT
				RTPsocket = new DatagramSocket(RTP_RCV_PORT);
				// set TimeOut value of the socket to 5msec.
				RTPsocket.setSoTimeout(5);
			} catch (SocketException se) {
				System.out.println("Socket exception: " + se);
				System.exit(0);
			}

			RTSPSeqNo = 1;
			this.sendRTSPRequest("SETUP");
			if (this.readRTSPResponse() != 200) {
				System.out.println("Invalid Server Response");
			} else {
				// change RTSP state and print new state
				state = READY;
				System.out.println("New RTSP state: READY");
			}



		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends a PLAY request to the server. This method is responsible for
	 * sending the request, receiving the response and, in case of a successful
	 * response, starting the RTP timer responsible for receiving RTP packets
	 * with frames.
	 * 
	 * @throws RTSPException
	 *             If there was an error sending or receiving the RTSP data, or
	 *             if the server did not return a successful response.
	 */
	public synchronized void play() throws RTSPException {

		// TODO
		System.out.println("Play Button pressed!"); 
		if (state == READY) 
		{
			//increase RTSP sequence number
			RTSPSeqNo++;

			//Send PLAY message to the server
			this.sendRTSPRequest("PLAY");
			if (this.readRTSPResponse() != 200) {
				System.out.println("Invalid Server Response");
			}
			else 
			{
				//change RTSP state and print out new state
				state= PLAYING;
				System.out.println("New RTSP state: PLAYING");

				//				this.receiveRTPPacket();
				//start the timer
				startRTPTimer();
				startPlayTimer();
				startTime = System.currentTimeMillis();
				//				this.startPlayTimer();

			}
		}//else if state != READY then do nothing
		//		System.out.println("AIWFAOWIEJFAWOIEJF");
	}

	/**
	 * Starts a timer that reads RTP packets repeatedly. The timer will wait at
	 * least MINIMUM_DELAY_READ_PACKETS_MS after receiving a packet to read the
	 * next one.
	 */
	private void startRTPTimer() {
		buf = new byte[BUFFER_LENGTH];
		packet = new DatagramPacket(buf, buf.length);
		rtpTimer = new Timer();
		rtpTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				receiveRTPPacket();
			}
		}, 0, MINIMUM_DELAY_READ_PACKETS_MS);
	}
	private void startPlayTimer() {
		playTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				playBuffer();
			}
		}, 0, 40);
	}

	private void playBuffer() {
		if(isPlaying==true) {
			Entry<Integer, RTPpacket> pair = packetMap.firstEntry();
			Integer k = pair.getKey();
			RTPpacket p = pair.getValue();
			Frame f = new Frame((byte)p.PayloadType, p.getMarker(), (short)p.SequenceNumber, p.TimeStamp, p.payload);
			session.processReceivedFrame(f);
			packetMap.remove(k);
			//			System.out.println(packetMap.size());
			System.out.println("Buffer Size: "+packetMap.size());
			if(packetMap.size()==0)isPlaying=false;

		} else {
			isPlaying=false;
			System.out.println("Buffer Size: "+packetMap.size());
		}
	}
	/**
	 * Receives a single RTP packet and processes the corresponding frame. The
	 * data received from the datagram socket is assumed to be no larger than
	 * BUFFER_LENGTH bytes. This data is then parsed into a Frame object (using
	 * the parseRTPPacket method) and the method session.processReceivedFrame is
	 * called with the resulting packet. In case of timeout no exception should
	 * be thrown and no frame should be processed.
	 */
	private void receiveRTPPacket() {
		// TODO
		try {

			if (packetMap.size()==25) {
				endTime = System.currentTimeMillis();
				elapsedPlayTime = endTime - startTime;
				howMany25Seconds++;
				mean25SFrames = elapsedPlayTime/howMany25Seconds;
				mostRecentRTT = mean25SFrames/25;
				if(estimatedRTT==0) estimatedRTT = mostRecentRTT;
				if(highestRTT<mostRecentRTT) highestRTT=mostRecentRTT;
				estimatedRTT =  ((1-alpha)*(double)estimatedRTT)+(alpha*(double)mostRecentRTT);
				RTT.add(mostRecentRTT);
				Iterator it = RTT.iterator();
				meanRTT = 0;
				while (it.hasNext()) {
					meanRTT += (Double) it.next();
				}
				meanRTT = meanRTT/RTT.size();

				System.out.println("EstimatedRTT: "+estimatedRTT);
				System.out.println("MostRecentRTT: "+mostRecentRTT);
				System.out.println("MeanRTT: "+meanRTT);
			}

			RTPsocket.receive(packet);
			RTPpacket newPacket = new RTPpacket(packet.getData(), packet.getLength());
			packetMap.put(newPacket.TimeStamp, newPacket);

			if(		packetMap.size()>25 &&
					(System.currentTimeMillis()-startTime)>(howMany25Seconds*mean25SFrames) && 
					isPlaying==false) {
				isPlaying=true;
				
			
				System.out.println("Buffer Size: "+packetMap.size());
				//				System.out.println("time for 25:"+(System.currentTimeMillis()-startTime));
			}

		} catch (SocketTimeoutException e ) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;

	}



	/**
	 * Sends a PAUSE request to the server. This method is responsible for
	 * sending the request, receiving the response and, in case of a successful
	 * response, cancelling the RTP timer responsible for receiving RTP packets
	 * with frames.
	 * 
	 * @throws RTSPException
	 *             If there was an error sending or receiving the RTSP data, or
	 *             if the server did not return a successful response.
	 */
	public synchronized void pause() throws RTSPException {

		// TODO
		//System.out.println("Pause Button pressed !");   

		if (state == PLAYING) 
		{
			//increase RTSP sequence number
			RTSPSeqNo ++;

			//Send PAUSE message to the server
			sendRTSPRequest("PAUSE");

			//Wait for the response 
			if (readRTSPResponse() != 200)
				System.out.println("Invalid Server Response");
			else 
			{
				//change RTSP state and print out new state
				state= READY;
				System.out.println("New RTSP state:READY");

				//stop the timer
				rtpTimer.cancel();
			}
		}
		//else if state != PLAYING then do nothing

	}

	/**
	 * Sends a TEARDOWN request to the server. This method is responsible for
	 * sending the request, receiving the response and, in case of a successful
	 * response, closing the RTP socket. This method does not close the RTSP
	 * connection, and a further SETUP in the same connection should be
	 * accepted. Also this method can be called both for a paused and for a
	 * playing stream, so the timer responsible for receiving RTP packets will
	 * also be cancelled.
	 * 
	 * @throws RTSPException
	 *             If there was an error sending or receiving the RTSP data, or
	 *             if the server did not return a successful response.
	 */
	public synchronized void teardown() throws RTSPException {

		// TODO
		System.out.println("Teardown Button pressed !");  

		//increase RTSP sequence number
		RTSPSeqNo ++;


		//Send TEARDOWN message to the server
		sendRTSPRequest("TEARDOWN");

		//Wait for the response 
		if (readRTSPResponse() != 200)
			System.out.println("Invalid Server Response");
		else 
		{     
			//change RTSP state and print out new state
			state=INIT;
			System.out.println("New RTSP state:INIT");
			this.RTPsocket.close();
			//stop the timer
			rtpTimer.cancel();

			//exit
			System.exit(0);
		}
	}

	/**
	 * Closes the connection with the RTSP server. This method should also close
	 * any open resource associated to this connection, such as the RTP
	 * connection, if it is still open.
	 */
	public synchronized void closeConnection() {
		// TODO
		try {
			this.RTSPBufferedReader.close();
			this.RTSPBufferedWriter.close();
			this.RTPsocket.close();
			socket.close();

			System.out.println("ALL CONNECTIONS CLOSED");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Parses an RTP packet into a Frame object.
	 * 
	 * @param packet
	 *            the byte representation of a frame, corresponding to the RTP
	 *            packet.
	 * @return A Frame object.
	 */
	private static Frame parseRTPPacket(byte[] packet) {
		System.out.println("parseRTPPacket");
		// TODO
		RTPpacket rtpp = new RTPpacket(packet, packet.length);
		//		Frame f = new Frame();
		return null; // Replace with a proper Frame
	}

	private void sendRTSPRequest(String requestType) {
		try {
			System.out.println("---------START OF REQUEST---------");
			// write request line
			String holder =requestType + " "+ videoFileName + " " + "RTSP/1.0" + CRLF;
			this.RTSPBufferedWriter.write(holder);
			System.out.print(holder);
			// write CSeq line;
			holder = "Cseq: " + RTSPSeqNo + CRLF;
			this.RTSPBufferedWriter.write(holder);
			System.out.print(holder);
			// Check if state is init
			if (state == INIT) {
				holder = "TRANSPORT: RTP/UDP; client_port= "+ RTP_RCV_PORT + CRLF + CRLF;
				this.RTSPBufferedWriter.write(holder);
				System.out.print(holder);
			} else {
				holder = "Session: " + RTSPid + CRLF+CRLF;
				this.RTSPBufferedWriter.write(holder);
				System.out.print(holder);
			}
			//			System.out.println(RTSPBufferedWriter.toString());
			System.out.println("------------END OF REQUEST------------");
			this.RTSPBufferedWriter.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int readRTSPResponse() {
		int reply_code = 0;

		String StatusLine;
		try {
			System.out.println("---------START OF RESPONSE-------");
			// Server Response
			StatusLine = this.RTSPBufferedReader.readLine();
			System.out.println(StatusLine);

			// Only try to read more lines if reply is 200
			reply_code = this.parseStatusResponse(StatusLine);
			if (reply_code == 200) {
				// Cseq
				StatusLine = this.RTSPBufferedReader.readLine();
				System.out.println(StatusLine);
				// Session ID
				StatusLine = this.RTSPBufferedReader.readLine();
				this.setRTSPid(StatusLine);
				System.out.println(StatusLine);
				StatusLine = this.RTSPBufferedReader.readLine();
				System.out.println(StatusLine);
			}

			System.out.println("---------END OF RESPONSE---------");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			//				e.printStackTrace();
		}
		//			StringTokenizer tokens = new StringTokenizer(StatusLine);
		//			tokens.nextToken(); // skip over the RTSP version
		//			reply_code = Integer.parseInt(tokens.nextToken());
		//
		//			// if reply code is OK get and print the 2 other lines
		//			if (reply_code == 200) {
		//				String SeqNumLine = RTSPBufferedReader.readLine();
		//				System.out.println(SeqNumLine);
		//
		//				String SessionLine = RTSPBufferedReader.readLine();
		//				System.out.println(SessionLine);
		//
		//				// if state == INIT gets the Session Id from the SessionLine
		//				if (state== INIT) {
		//					tokens = new StringTokenizer(SessionLine);
		//					tokens.nextToken(); // skip over the Session:
		//					RTSPid = Integer.parseInt(tokens.nextToken());
		//				}
		//				
		//			} else {
		//				String one = RTSPBufferedReader.readLine();
		//				System.out.println(one);
		//				String two = RTSPBufferedReader.readLine();
		//				System.out.println(two);
		//				String three = RTSPBufferedReader.readLine();
		//				System.out.println(three);
		//			}
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}
		return (reply_code);
	}

	private int parseStatusResponse(String s) {
		int status;
		StringTokenizer tokens = new StringTokenizer(s);
		tokens.nextToken();
		status = Integer.valueOf(tokens.nextToken());
		return status;
	}
	private int setRTSPid(String s) {
		StringTokenizer tokens = new StringTokenizer(s);
		tokens.nextToken();
		RTSPid = Integer.valueOf(tokens.nextToken());
		return RTSPid;
	}
}


