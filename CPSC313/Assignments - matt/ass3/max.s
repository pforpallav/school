.pos 0x100
max:             irmovl $a, %ebx          # ebx = &a
                 mrmovl 0x0(%ebx), %ebx   # ebx = a
                 irmovl $aSize, %ecx      # ecx = &aSize
                 mrmovl 0x0(%ecx), %ecx   # ecx = aSize = cnt
                 irmovl $0x80000000, %eax # eax =small num = max
                 irmovl $0x4, %edi        # edi = 4
loop:            irmovl $0x1, %edx        # edx = 1
                 subl   %edx, %ecx        # cnt -= 1
                 jl     done              # goto done if cnt < 0
                 mrmovl 0x0(%ebx), %edx   # edx = *a
                 addl   %edi, %ebx        # a++
                 rrmovl %edx, %esi        # esi = *a = t
                 subl   %eax, %esi        # t = *a - max
                 jle    loop              # goto loop if  *a <= max
                 rrmovl %edx, %eax        # max = *a
                 jmp    loop              # goto loop
done:            irmovl $result, %ebx     # ebx = &result
                 rmmovl %eax, 0x0(%ebx)   # result = min
                 halt                     
.pos 0x1000
a:               .long 0x00002000         # int *a = aData
aSize:           .long 0x0000000a         
result:          .long 0x00000000         
.pos 0x2000
aData:           .long 0x0000000e         # aData[0]
                 .long 0x00000003         # aData[1]
                 .long 0x0000001d         # aData[2]
                 .long 0x0000000f         # aData[3]
                 .long 0x00000010         # aData[4]
                 .long 0x000002be         # aData[5]
                 .long 0x00000141         # aData[6]
                 .long 0x0000002b         # aData[7]
                 .long 0xffffff9c         # aData[8]
                 .long 0x00000020         # aData[9]
