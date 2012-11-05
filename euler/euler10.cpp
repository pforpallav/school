#include <iostream>
using namespace std;

int isPrime(unsigned long long a) {
	for (unsigned long long i=a-1; i>1; i--) 
	{
		if(a%i==0) {
			return 0;
		}
	}
	return 1;
}

int main() {
	unsigned long long sum = 0;
	for (unsigned long long i=2; i<=2000000; i++) 
	{
		if(isPrime(i)) {
			sum += i;
		}
	}
	cout << sum << endl;
	return 1;
}