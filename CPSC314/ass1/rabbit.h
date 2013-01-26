#pragma once
class rabbit
{
private:
	enum side {left, right};
	enum movement {up,down};
	movement dirHead;
	movement dirLeftArm;
	movement dirRightArm;
	movement dirLeftEar;
	movement dirRightEar;
	movement dirRear;
	movement dirBodyCurl;
	movement dirJump;
	bool smoothAnimation;
	bool frame;
	bool head;
	bool leftArm;
	bool rightArm;
	bool leftEar;
	bool rightEar;
	bool rear;
	bool bodyCurl;
	bool jump;
	void drawCube();
	void drawTail();
	void drawLeg(side);
	void drawArm(side);
	void drawLowerBody();
	void drawTorso();
	void drawUpperBody();
	void drawNeck();
	void drawHead();
	void drawEar(side);
	void drawEye(side);
	void moveHead();
	void moveArm(side);

public:

	rabbit(void);
	~rabbit(void);
	
	void toggleSmoothAnimation();
	void toggleWire();	
	void toggleHead();
	void toggleLeftArm();
	void toggleRightArm();
	void toggleRightEar();
	void toggleLeftEar();
	void toggleRear();
	void toggleBodyCurl();
	void toggleJump();

	void draw();

	
};

