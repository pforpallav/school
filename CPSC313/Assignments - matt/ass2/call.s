.pos 0x100
	irmovl	$0x0, %eax
	irmovl	$0x4, %ecx
	call test1
	call 0x163(%ecx), %edx
	halt
	irmovl	$0x0, %eax
	irmovl	$0x0, %eax
	irmovl	$0x0, %eax
	irmovl	$0x0, %eax
	irmovl	$0x0, %eax
	irmovl	$0x0, %eax
	irmovl	$0x0, %eax
	irmovl	$0x0, %eax
	irmovl	$0x0, %eax
	irmovl	$0x0, %eax
	irmovl	$0x0, %eax
	irmovl	$0x0, %eax
	
test1:
	irmovl	$0x7, %eax
	ret
test2:
	irmovl	$0x8, %eax
	ret
	
