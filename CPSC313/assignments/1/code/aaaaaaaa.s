#
# Calling conventions:
#     %eax, %ebx, %ecx, %edx can be used by subroutines without saving.
#     %ebp, %esi, %edi must be saved and restored if they are used.
#     %esp can not be used except for its normal use as a stack pointer.
#
#     argument are passed through registers %eax, %ebx, except for subroutine
#     heapify_node_helper (see its documentation).
#
#     values are returned through %eax
#
.pos 0x100

main:	irmovl bottom,  %esp     # initialize stack

	xorl   %eax, %eax        # %eax = 0
	mrmovl size(%eax), %eax  # %eax = size
	irmovl $1, %ebx
	subl   %ebx, %eax        # %eax = size - 1
	
	call   heapsort
	halt

#
# Swap:
#    %eax: index1
#    %ebx: index2
#
swap:	addl   %eax, %eax	# Offset is 4 times the index
	addl   %eax, %eax
	addl   %ebx, %ebx	# Offset is 4 times the index
	addl   %ebx, %ebx
	
	mrmovl heap(%eax), %ecx # tmp = heap[index1]
	mrmovl heap(%ebx), %edx # heap[index1] = heap[index2]
	rmmovl %edx, heap(%eax)
	rmmovl %ecx, heap(%ebx) # heap[index2] = tmp

	ret

#
# Check_child
#     %ecx: child index
#     %esi: index of highest
#     %ebp: last
# Returns:
#     %eax: true if child <= last && heap[highest] < heap[child]
#
check_child:
	pushl  %edi
	xorl   %eax, %eax       # set return value to false.

	rrmovl %ecx, %edi       # if child > last, skip
	subl   %ebp, %edi
	jg     check_child_finish

	rrmovl %esi, %edi       # %edi = heap[highest]
	addl   %edi, %edi
	addl   %edi, %edi
	mrmovl heap(%edi), %edi

	rrmovl %ecx, %edx       # %edx = heap[child]
	addl   %edx, %edx
	addl   %edx, %edx
	mrmovl heap(%edx), %edx

	irmovl $1,   %ebx
	subl   %edi, %edx      # if heap[child] > heap[highest], return 1
	cmovg  %ebx, %eax

check_child_finish:
	popl %edi
	ret

#
# Heapify_node
#     %eax: index
#     %ebx: last
#
# Local variables:
#     %ecx: left_child, right_child, need_to_swap
#     %esi: highest
#     %edi: index
#     %ebp: last
#
heapify_node:
	pushl  %edi             # Save %edi and use it to store index
	rrmovl %eax, %edi
	pushl  %esi		# Save %esi and use it to store highest
	rrmovl %eax, %esi
	pushl  %ebp
	rrmovl %ebx, %ebp

heapify_loop:
	rrmovl %edi, %ecx       # left_child = 2 * index + 1
	addl   %ecx, %ecx
	irmovl $1,   %edx
	addl   %edx, %ecx

	call   check_child
	andl   %eax, %eax
	cmovne %ecx, %esi       # highest = left_child (if condition ok)

	irmovl $1, %edx		# right_child = 2 * index + 2
	addl   %edx, %ecx

	call   check_child
	andl   %eax, %eax
	cmovne %ecx, %esi       # highest = right_child (if condition ok)

heapify_skip2:
	rrmovl %esi, %eax
	subl   %edi, %eax
	je     heapify_finish

	rrmovl %esi, %eax
	rrmovl %edi, %ebx
	call   swap

	rrmovl %esi, %edi
	jmp    heapify_loop

heapify_finish:
	popl   %ebp
	popl   %esi
	popl   %edi
	ret
	
	
# Heapify_array
#     %eax: last
#
heapify_array:
	pushl  %esi             # Save %esi and use it to store 'last'
	rrmovl %eax, %esi
	pushl  %edi             # Save %edi before using it for i

	irmovl $1, %ebx		# %eax = last - 1
	subl   %ebx, %eax
	irmovl $2, %ebx		# %eax = (last - 1)/2
	divl   %ebx, %eax
	rrmovl %eax, %edi       # i = %eax
	
ha_loop:
	andl   %edi, %edi       # check if i < 0
	jl     ha_finish

	rrmovl %edi, %eax       # Set %eax = i, %ebx = last      
	rrmovl %esi, %ebx
	call   heapify_node     # Heapify the node

	irmovl $1, %eax         # i--
	subl   %eax, %edi
	jmp    ha_loop

ha_finish:
	popl   %edi
	popl   %esi
	ret
	
#
# Extract_max
#     %eax: last
#
extract_max:
	pushl  %esi		# Save %esi before using it for max

	xorl   %ebx, %ebx       # max = heap[0]
	mrmovl heap(%ebx), %esi

	rrmovl %eax, %edx
	addl   %edx, %edx       # %ecx = heap[last]
	addl   %edx, %edx
	mrmovl heap(%edx), %ecx 
	rmmovl %ecx, heap(%ebx) # heap[0] = %ecx

	rrmovl %eax, %ebx	# %ebx = last - 1
	irmovl $1, %ecx
	subl   %ecx, %ebx
	xorl   %eax, %eax       # %eax = 0
	call   heapify_node     # Heapify the root

	rrmovl %esi, %eax       # Set return value to max
	popl   %esi
	ret

#
# Heapsort
#    %eax: last
#	 %eax, %ebx, %ecx, %edx
heapsort:
	andl 	%eax, %eax
	rrmovl	%eax, %ecx
	jl		L1
	
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	call	heapify_array
	popl 	%ebx
	popl	%edx
	popl	%ecx

L3:
	rrmovl	%ecx, %edx
	
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	call	extract_max
	popl 	%ebx
	popl	%edx
	popl	%ecx

	irmovl	$1,	%ebx
	subl	%ebx, %ecx
	pushl	%ebp
	rrmovl	%ecx, %ebp
	addl	%ebx, %ebp
	popl	%ebp
	pushl	%ebx
	irmovl	$4, %ebx
	popl	%ebx
	rmmovl	%eax, heap(%ecx)
	je		L3
L1:
	ret
	


###
### THIS PART TO BE COMPLETED BY THE STUDENT.
###

#
# Array to sort
#
.pos 0x1000
heap:	.long 3
        .long 14
        .long 5
        .long 2
        .long 22
	.long 16
	.long 11
	.long 12
	.long 9
	.long 13
	.long 15
	.long 1
	.long 4
	.long 18
size:   .long 14
	
#
# Stack (32 thirty-two bit words is more than enough here).
#
.pos 0x3000
top:	            .long 0x00000000,0x20     # top of stack.
bottom:             .long 0x00000000          # bottom of stack.


