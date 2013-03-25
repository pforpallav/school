======================
STUDENTS:
-----------------
1)	Name: 	Evan Louie
	SID:	72210099
	CSID:	m6d7
2)	Name:	Theo Ng
	SID:	70857099
	CSID:	y4s7
======================

===================
INSTRUCTIONS
===================
I made an additional class called RTPpacket. This class takes in a DatagramPacket.getData() and a DatagramPacket.getLength(); the RTPpacket class acts as autoparser and getter for the header data and package.



=====================================
WHAT WE TRIED:
=====================================

	1) Use dual buffers; fill one till full and begin to play packets from it. While playing from one (and removing said packets after being playing), buffer new packets into the second buffer and switch to it when the first buffer is empty.
		- Fixed buffering issues (was able to successfully pop and play from one buffer while filling another)
		- Playback issues still occured (too fast/too slow), FIXED in 3)

		Problems:
			- Threaded implementation of receiveRTPPacket() made it impossible to create/call a recursive function which would allow to indiscriminatley swtich between buffers. (ala bufferedPlay(b1,b2) couldn't call bufferedPlay(b2,b1) as multiple threads from receiveRTPPacket() would call the function and the function calls would propegate till playback was hyper fast and buffering couldn't keep up)

	2) Single buffer, buffer and playback from same buffer in asynchronously.
		- Buffering still broken
		- Playback still broken

		Problems:
			- Multi threaded implementaion of receiveRTPPacket() made buffer unable to buffer simultaneously while playing back, causing strange behaviour depeding on the machine run on (how many cores the CPU had on the particular machine).

	3) Single buffer, buffering and playback are functions call autonomously from one another using two separate timers. Buffering times are calculated using the EstimatedRTT equation learned in class: EtimatedRTT=(1-a)*EstimatedRTT+(a*MostRecentRTT)
			- Fixed playback speed issues
			- Buffering still broken

		Problems:
			- For some reason when playing back from a buffer, threads aren't able to add/remove from the same buffer. Causing buffering times to occur.
			