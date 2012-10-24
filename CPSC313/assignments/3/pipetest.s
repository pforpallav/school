.pos 0x100
fwdOrder:        irmovl $0x1, %eax        
                 irmovl $0x2, %eax        
                 rrmovl %eax, %ecx        # data-forwarding order (1)
                 irmovl $0x1, %edx        
                 irmovl $0x2, %edx        
                 irmovl $0x3, %edx        
                 rrmovl %edx, %ebx        # data-forwarding order (2)
                 halt                     
.pos 0x200
srcAHzd:         irmovl $0x0, %eax        
                 irmovl $0x0, %ecx        
                 irmovl $0x0, %edx        
                 irmovl $0x0, %ebx        
                 irmovl $0x1, %eax        # r-r data hazard on srcA (1)
                 addl   %eax, %ebx        
                 irmovl $0x2, %ecx        # r-r data hazard on srcA (2)
                 nop                      
                 addl   %ecx, %ebx        
                 irmovl $0x3, %edx        # r-r data hazard on srcA (3)
                 nop                      
                 nop                      
                 addl   %edx, %ebx        
                 halt                     
.pos 0x300
sBHazard:        irmovl $0x0, %eax        
                 irmovl $0x0, %ecx        
                 irmovl $0x0, %edx        
                 irmovl $0x10, %ebx       
                 irmovl $0x1, %eax        # r-r data hazard on srcB (1)
                 addl   %ebx, %eax        
                 irmovl $0x2, %ecx        # r-r data hazard on srcB (2)
                 nop                      
                 addl   %ebx, %ecx        
                 irmovl $0x3, %edx        # r-r data hazard on srcB (3)
                 nop                      
                 nop                      
                 addl   %ebx, %edx        
                 halt                     
.pos 0x400
aLoadUse:        irmovl $0x0, %eax        
                 irmovl $0x0, %ecx        
                 irmovl $0x0, %edx        
                 irmovl $0x0, %ebx        
                 irmovl $x, %edi          
                 mrmovl 0x0(%edi), %eax   # load-use data hazard on srcA (1)
                 addl   %eax, %ebx        
                 mrmovl 0x0(%edi), %ecx   # load-use data hazard on srcA (2)
                 nop                      
                 addl   %ecx, %ebx        
                 mrmovl 0x0(%edi), %edx   # load-use data hazard on srcA (3)
                 nop                      
                 nop                      
                 addl   %edx, %ebx        
                 halt                     
.pos 0x500
bLoadUse:        irmovl $0x0, %eax        
                 irmovl $0x0, %ecx        
                 irmovl $0x0, %edx        
                 irmovl $0x10, %ebx       
                 irmovl $x, %edi          
                 mrmovl 0x0(%edi), %eax   # load-use data hazard on srcB (1)
                 addl   %ebx, %eax        
                 mrmovl 0x0(%edi), %ecx   # load-use data hazard on srcB (2)
                 nop                      
                 addl   %ebx, %ecx        
                 mrmovl 0x0(%edi), %edx   # load-use data hazard on srcB (3)
                 nop                      
                 nop                      
                 addl   %ebx, %edx        
                 halt                     
.pos 0x600
takenJmp:        irmovl $0x0, %eax        
                 irmovl $0x0, %ecx        
                 irmovl $0x0, %edx        
                 irmovl $0x0, %ebx        
                 irmovl $0x0, %esp        
                 andl   %eax, %eax        
                 je     JT                # taken jump
                 irmovl $0x1, %ecx        # should not execute
                 irmovl $0x1, %edx        # should not execute
                 nop                      
                 halt                     
JT:              irmovl $0x1, %ebx        # should execute
                 irmovl $0x1, %esp        # should execute
                 halt                     
.pos 0x700
notTkJmp:        irmovl $0x0, %eax        
                 irmovl $0x0, %ecx        
                 irmovl $0x0, %edx        
                 irmovl $0x0, %ebx        
                 irmovl $0x0, %esp        
                 andl   %eax, %eax        
                 jne    JNT               # not-taken jump
                 irmovl $0x1, %ecx        # should execute
                 irmovl $0x1, %edx        # should execute
                 halt                     
JNT:             irmovl $0x1, %ebx        # should not execute
                 irmovl $0x1, %esp        # should not execute
                 halt                     
.pos 0x800
callRtn:         irmovl $stack, %esp      
                 irmovl $0x0, %eax        
                 irmovl $0x0, %ecx        
                 irmovl $0x0, %edx        
                 call   CR                
                 irmovl $0x1, %ecx        # should execute
                 halt                     
CR:              irmovl $0x1, %eax        
                 ret                      # return
                 irmovl $0x1, %edx        # should not execute
                 halt   
.pos 0x900
cmov:            irmovl $1, %eax
                 irmovl $2, %ebx
                 xorl   %ecx, %ecx
                 cmovne %eax, %ebx
                 addl   %ebx, %ebx
                 halt                  
.pos 0x1000
x:               .long 0x0000000a         
.pos 0xf000
                 .long 0x00000000         # runtime stack
                 .long 0x00000000         
                 .long 0x00000000         
                 .long 0x00000000         
                 .long 0x00000000         
stack:           .long 0x00000000         
