	.file	"heapsort.c"
	.text
	.p2align 4,,15
	.globl	heapsort
	.type	heapsort, @function
heapsort:
.LFB11:
	.cfi_startprocq				#START HERE
	pushq	%rbx				#Save current value of rbx
	.cfi_def_cfa_offset 16		#Can ignore
	.cfi_offset 3, -16			#Can ignore
	movl	%edi, %ebx			#moves the value from %edi into %ebx
	call	heapify_array		#heapify_array(last), Calls heapify_array function
	testl	%ebx, %ebx			# i >= 0, "ands" i with itself, to check if i is greater or equal to zero
	js	.L1						#Finish, if the previous operation is signed, i.e. negative
	.p2align 4,,10				#Can ignore
	.p2align 3					#Can ignore
.L3:
	movl	%ebx, %edi			#Moves the variable i to be used (preparation for extract_max(i))
	call	extract_max			#extract_max(i), Calls extract_max function
	movslq	%ebx, %rdx			#Converts the value stored in ebx into 64bit to register rdx
	subl	$1, %ebx			#i--, Decrements i by 1
	cmpl	$-1, %ebx			#i >= 0, Actually compares if i is equal to -1
	movl	%eax, heap(,%rdx,4)	#heap[i] = extract_max(i), Moves the value retieved from extract_max(i) to heap[i]
	jne	.L3						#Repeats the loop, if compare was not equal
.L1:
	popq	%rbx				#Restore old value of rbx
	.cfi_def_cfa_offset 8		#Can ignore
	ret							#return, i.e. ends function
	.cfi_endproc				#END HERE
.LFE11:
	.size	heapsort, .-heapsort
	.ident	"GCC: (GNU) 4.6.3 20120306 (Red Hat 4.6.3-2)"
	.section	.note.GNU-stack,"",@progbits
