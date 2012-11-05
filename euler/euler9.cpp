#include <iostream>
using namespace std;

int isValid(int a, int b, int c) {
	if (a < b && b < c)
		return 1;
	return 0;
}

int isPythag(int a, int b, int c) {
	int a_squared = a*a;
	int b_squared = b*b;
	int c_squared = c*c;
	if (a_squared + b_squared == c_squared)
		return 1;
	return 0;
}

int sum(int a, int b, int c) {
	if (a + b + c == 1000){
		return 1;
	}
	return 0;
}
int main() {

	for (int a=0; a<=1000; a++) 
	{
		for (int b=0; b<=1000; b++) 
		{
			for (int c=0; c<=1000; c++) 
			{
				if (isValid(a,b,c)) 
				{
					if (isPythag(a,b,c)) 
					{
						if(sum(a,b,c)) 
						{
							cout<<"a="<<a<<endl<<"b="<<b<<endl<<"c="<<c<< endl;
							int product = a*b*c;
							cout<<"abc="<<product<<endl;
						}
					}
				}
			}
		}
	}

	return 0;
}