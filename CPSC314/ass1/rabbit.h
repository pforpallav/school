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
	movement dirLeftLeg;
	movement dirRightLeg;
	movement dirUpperBody;
	movement dirLowerBody;
	movement dirUpperBodyCurl;

	bool smoothAnimation;
	bool frame;
	bool head;
	bool leftArm;
	bool rightArm;
	bool leftEar;
	bool rightEar;
	bool rear;
	bool bodyCurl;
	bool leftLeg;
	bool rightLeg;
	bool jump;
	bool uBody;
	bool lBody;
	bool upperBodyCurl;

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
	void moveLeg(side);
	void moveRear();
	void moveCurl();
	void moveUpperCurl();
	void moveJump();
	void openUpperBody();
	void openLowerBody();
	void moveEar(side);

public:

	rabbit(void);
	~rabbit(void);
	
	void toggleSmoothAnimation();
	void toggleWire();	
	void toggleHead();
	void toggleLeftArm();
	void toggleRightArm();
	void toggleLeftLeg();
	void toggleRightLeg();
	void toggleRightEar();
	void toggleLeftEar();
	void toggleRear();
	void toggleBodyCurl();
	void toggleUpperCurl();
	void toggleJump();
	void toggleUpperBody();
	void toggleLowerBody();

	void draw();

	
};

