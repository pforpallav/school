.pos 0x100
testx4:
	irmovl $0x28, %ecx
	irmovl $0x0, %edx
	irmovl $0x1, %ebx
	rmmovl %ecx, TheArray(%edx,4)
	rmmovl %ecx, TheArray(%ebx,4)
	mrmovl TheArray(%edx,4), %eax
	irmovl $0x29, %ecx
	irmovl $0x2, %edx
	irmovl $0x3, %ebx
	rmmovl %ecx, TheArray(%edx,4)
	rmmovl %ecx, TheArray(%ebx,4)
	mrmovl TheArray(%edx,4), %esi
	irmovl $0x2a, %ecx
	irmovl $0x4, %edx
	irmovl $0x5, %ebx
	rmmovl %ecx, TheArray(%edx,4)
	rmmovl %ecx, TheArray(%ebx,4)
	mrmovl TheArray(%edx,4), %edi


testx2:
	irmovl $0x14, %ecx
	irmovl $0x0, %edx
	irmovl $0x2, %ebx
	rmmovl %ecx, TheArray(%edx,2)
	rmmovl %ecx, TheArray(%ebx,2)
	mrmovl TheArray(%edx,2), %eax
	irmovl $0x15, %ecx
	irmovl $0x4, %edx
	irmovl $0x6, %ebx
	rmmovl %ecx, TheArray(%edx,2)
	rmmovl %ecx, TheArray(%ebx,2)
	mrmovl TheArray(%edx,2), %esi
	irmovl $0x16, %ecx
	irmovl $0x8, %edx
	irmovl $0xa, %ebx
	rmmovl %ecx, TheArray(%edx,2)
	rmmovl %ecx, TheArray(%ebx,2)
	mrmovl TheArray(%edx,2), %edi
	
testx1:
	irmovl $0xa, %ecx
	irmovl $0x0, %edx
	irmovl $0x4, %ebx
	rmmovl %ecx, TheArray(%edx,1)
	rmmovl %ecx, TheArray(%ebx,1)
	mrmovl TheArray(%edx,1), %eax
	irmovl $0xb, %ecx
	irmovl $0x8, %edx
	irmovl $0xc, %ebx
	rmmovl %ecx, TheArray(%edx,1)
	rmmovl %ecx, TheArray(%ebx,1)
	mrmovl TheArray(%edx,1), %esi
	irmovl $0xc, %ecx
	irmovl $0x10, %edx
	irmovl $0x14, %ebx
	rmmovl %ecx, TheArray(%edx,1)
	rmmovl %ecx, TheArray(%ebx,1)
	mrmovl TheArray(%edx,1), %edi

testxReg:
	irmovl $0x0, %ecx
	irmovl $0x0, %edx
	irmovl $0x4, %ebx
	rmmovl %ecx, TheArray(%edx)
	rmmovl %ecx, TheArray(%ebx)
	mrmovl TheArray(%edx), %eax
	irmovl $0x1, %ecx
	irmovl $0x8, %edx
	irmovl $0xc, %ebx
	rmmovl %ecx, TheArray(%edx)
	rmmovl %ecx, TheArray(%ebx)
	mrmovl TheArray(%edx), %esi
	irmovl $0x2, %ecx
	irmovl $0x10, %edx
	irmovl $0x14, %ebx
	rmmovl %ecx, TheArray(%edx)
	rmmovl %ecx, TheArray(%ebx)
	mrmovl TheArray(%edx), %edi

testxInvalid:
	#uncomment the below code and attempt to open Simple Machine one at a time, it should fail due to an illegal scale
	#rmmovl %ecx, TheArray(%edx,3)
	#mrmovl TheArray(%edx,7), %edi
	halt
	
	

.align 4
.pos 0x1000
TheArray:	.long	7
	.long	7
	.long	7
	.long	7
	.long	7
	.long	7
