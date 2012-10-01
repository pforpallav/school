	.file	"heapsort.c"
	.text
	.p2align 4,,15
	.globl	heapsort
	.type	heapsort, @function
heapsort:
.LFB11:
	.cfi_startproc
	testl	%edi, %edi			# if (last < 0)				
	pushq	%rbx				# push %rbx to top of stack
	.cfi_def_cfa_offset 16	
	.cfi_offset 3, -16
	movl	%edi, %ebx			# %ebx = %edi = last
	js	.L1						# if (Last > 0); jump L1
	call	heapify_array		# heapify_array(last)
	.p2align 4,,10			
	.p2align 3
.L3:
	movl	%ebx, %edi			# i = last (during first iteration, otherwise gets i--)
	call	extract_max			# %eax = extract_max(i)
	movslq	%ebx, %rdx			# Sign extend rdx
	subl	$1, %ebx			# i= i-1	
	cmpl	$-1, %ebx			# if (i = -1)
	movl	%eax, heap(,%rdx,4)	# heap[i] = extract_max(i)
	jne	.L3						# if (cmpl); jump L3
.L1:
	popq	%rbx				# Restore old %rbx
	.cfi_def_cfa_offset 8		
	ret							# return
	.cfi_endproc
.LFE11:
	.size	heapsort, .-heapsort
	.ident	"GCC: (SUSE Linux) 4.6.2"
	.section	.comment.SUSE.OPTs,"MS",@progbits,1
	.string	"Ospwg"
	.section	.note.GNU-stack,"",@progbits
