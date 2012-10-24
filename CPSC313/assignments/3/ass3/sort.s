.pos 0x100
sort:            irmovl $aSize, %eax      # eax = &aSize
                 mrmovl 0x0(%eax), %eax   # eax = aSize = i
                 irmovl $a, %ecx          # ecx = &a
                 mrmovl 0x0(%ecx), %ecx   # ecx = a
outer:           irmovl $0x1, %edi        # edi = 1
                 subl   %edi, %eax        # i -= 1
                 jl     done              # goto done if i < 0
                 mrmovl 0x0(%ecx), %ebx   # ebx = *a
                 rrmovl %eax, %edx        # j = i
                 rrmovl %ecx, %esi        # b = a
                 irmovl $0x4, %edi        # edi = 4
                 addl   %edi, %esi        # b++
inner:           irmovl $0x1, %edi        # edi = 1
                 subl   %edi, %edx        # j--
                 jl     endInner          # goto endInner if j<0
                 mrmovl 0x0(%esi), %edi   # edi = *b
                 rrmovl %edi, %ebp        # t = *b
                 subl   %ebx, %ebp        # t = *b - *a
                 jge    skipSwap          # goto skipSwap if *b >= *a
                 rmmovl %ebx, 0x0(%esi)   # *b = *a
                 rmmovl %edi, 0x0(%ecx)   # *a = *b
                 rrmovl %edi, %ebx        # ebx = *b
skipSwap:        irmovl $0x4, %edi        # edi = 4
                 addl   %edi, %esi        # b++
                 jmp    inner             # goto inner
endInner:        irmovl $0x4, %edi        # edi = 4
                 addl   %edi, %ecx        # a++
                 jmp    outer             # goto outer
done:            halt                     
.pos 0x1000
a:               .long 0x00002000         # int *a
aSize:           .long 0x0000000a         # int aSize
.pos 0x2000
aData:           .long 0x00000007         # aData[0]
                 .long 0x00000003         # aData[1]
                 .long 0x00000004         # aData[2]
                 .long 0x0000000a         # aData[3]
                 .long 0x00000005         # aData[4]
                 .long 0x00000008         # aData[5]
                 .long 0x00000009         # aData[6]
                 .long 0x00000001         # aData[7]
                 .long 0x00000006         # aData[8]
                 .long 0x00000002         # aData[9]
