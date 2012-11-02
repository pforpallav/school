.pos 0x100
iaddl:
	irmovl	$0x7, %eax
	iaddl 	$0x4, %eax
	irmovl	$0xb, %ecx
	subl 	%eax, %ecx
	jne 	iadderr
	iaddl 	$0xc, %eax
	irmovl	$0x17, %ecx
	subl 	%eax, %ecx
	jne 	iadderr
	
isub:
	isubl	$0x2, %eax
	irmovl	$0x15, %ecx
	subl 	%eax, %ecx
	jne 	isuberr
	isubl 	0x15, %eax
	jne 	isuberr
	isubl 	0x4, %eax
	irmovl	$0xfffffffc, %ecx
	subl 	%eax, %ecx
	jne 	isuberr
	isubl 	$0xfffffff3, %eax
	irmovl	$0x9, %ecx
	subl 	%eax, %ecx
	jne 	isuberr

imul:
	imull	$0x4, %eax
	irmovl	$0x24, %ecx
	subl 	%eax, %ecx
	jne 	imulerr
	imull	$0x5, %eax
	irmovl	$0xB4, %ecx
	subl 	%eax, %ecx
	jne 	imulerr
	imull	$0x0, %eax
	jne 	imulerr
	
idivl:
	iaddl	$0xB4, %eax
	idivl	$0x3, %eax
	irmovl	$0x3c, %ecx
	subl 	%eax, %ecx
	jne 	idiverr
	idivl	$0x7, %eax
	irmovl	$0x8, %ecx
	subl 	%eax, %ecx
	jne 	idiverr
	idivl	$0x0, %eax
	irmovl	$0x8, %ecx
	subl 	%eax, %ecx
	jne 	idiverr

iandl:
	isubl	$0x1, %eax
	iandl	$0x5, %eax
	irmovl	$0x5, %ecx
	subl 	%eax, %ecx
	jne 	ianderr
	iandl	$0x4, %eax
	irmovl	$0x4, %ecx
	subl 	%eax, %ecx
	jne 	ianderr
	iandl	$0x4, %eax
	irmovl	$0x4, %ecx
	subl 	%eax, %ecx
	jne 	ianderr
	iandl	$0x0, %eax
	jne 	ianderr

ixorl:
	iaddl 	$0x7, %eax
	ixorl	$0x9, %eax
	irmovl	$0xe, %ecx
	subl 	%eax, %ecx
	jne 	ixorerr
	ixorl	$0xe, %eax
	jne 	ixorerr
	ixorl	$0x9, %eax
	irmovl	$0x9, %ecx
	subl 	%eax, %ecx
	jne 	ixorerr
	
imodl:

	imodl	$0x6, %eax
	irmovl	$0x3, %ecx
	subl 	%eax, %ecx
	jne 	imoderr
	imodl	$0x2, %eax
	irmovl	$0x1, %ecx
	subl 	%eax, %ecx
	jne 	imoderr
	iaddl 	$0x7, %eax
	imodl	$0x0, %eax
	irmovl	$0x8, %ecx
	subl 	%eax, %ecx
	jne 	imoderr
	imodl	$0xfffffff3, %eax
	irmovl	$0x8, %ecx
	subl 	%eax, %ecx
	jne 	imoderr
	imodl	$0x1, %eax
	halt
iadderr:
	irmovl $0x1, %eax
	halt
	
isuberr:
	irmovl $0x2, %eax
	halt

imulerr:
	irmovl $0x3, %eax
	halt

idiverr:
	irmovl $0x4, %eax
	halt

ianderr:
	irmovl $0x5, %eax
	halt
	
ixorerr:
	irmovl $0x6, %eax
	halt
	
imoderr:
	irmovl $0x7, %eax
	halt