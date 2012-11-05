#include <iostream>
using namespace std;

unsigned long long triangulate(unsigned long long i) {
	unsigned long long sum = 0;
	unsigned long long current = i;
	while (current > 0) {
		sum += current;
		current--;
	}
	return sum;
}

unsigned long long countDivisors(unsigned long long i) {
	unsigned long long count = 0;
	unsigned long long current = i;
	while (current>0) {
		if(i%current==0) {
			count++;
			if (current*current==i) {
				count++;

			}
		}
		current--;
	}
	return count;
}

int main() {
	unsigned long long current = 0;
	unsigned long long count = 0;
	while (count <=500) {
		current++;
		if (countDivisors(triangulate(current))>count) {
			count = countDivisors(triangulate(current));
			cout << current << ":" << count <<endl;
		}
	}
	cout<<current;
}