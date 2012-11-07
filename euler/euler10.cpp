#include <iostream>
using namespace std;

bool isPrime(unsigned long long n) {
	if( n <= 1 )
		return false;
	unsigned long long half = n / 2;
	for( unsigned long long i = 2; i <= half; i++ )
		if( n % i == 0 )
			return false;
	return true;
}

int main() {
	unsigned long long sum = 0;
	for (unsigned long long i=2; i<=2000000; i++) 
	{
		if(isPrime(i)) {
			sum += i;
			cout<<sum<<endl;
		}
	}
	cout << sum << endl;
	return 1;
}