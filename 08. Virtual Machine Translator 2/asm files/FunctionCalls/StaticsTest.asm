@256
D=A
@SP
M=D
@RETURN_ADDR0
D=A
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@5
D=D+A
@SP
D=M-D
@ARG
M=D
@SP
D=M
@LCL
M=D
@Sys.init
0;JMP
(RETURN_ADDR0)
//function Class1.set 0
(Class1.set)
//push argument 0
@0
D=A
@ARG
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
//pop static 0
@SP
M=M-1
A=M
D=M
@Class1-0
M=D
//push argument 1
@1
D=A
@ARG
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
//pop static 1
@SP
M=M-1
A=M
D=M
@Class1-1
M=D
//push constant 0
@0
D=A
@SP
A=M
M=D
@SP
M=M+1
//return
@LCL
D=M
@R12
M=D
@5
D=D-A
A=D
D=M
@R14
M=D
@ARG
D=M
@0
D=D+A
@R13
M=D
@SP
AM=M-1
D=M
@R13
A=M
M=D
@ARG
D=M
@SP
M=D+1
@R12
D=M-1
AM=D
D=M
@THAT
M=D
@R12
D=M-1
AM=D
D=M
@THIS
M=D
@R12
D=M-1
AM=D
D=M
@ARG
M=D
@R12
D=M-1
AM=D
D=M
@LCL
M=D
@R14
A=M
0;JMP
//function Class1.get 0
(Class1.get)
//push static 0
@Class1-0
D=M
@SP
A=M
M=D
@SP
M=M+1
//push static 1
@Class1-1
D=M
@SP
A=M
M=D
@SP
M=M+1
//sub
@SP
AM=M-1
D=M
A=A-1
M=M-D
//return
@LCL
D=M
@R12
M=D
@5
D=D-A
A=D
D=M
@R14
M=D
@ARG
D=M
@0
D=D+A
@R13
M=D
@SP
AM=M-1
D=M
@R13
A=M
M=D
@ARG
D=M
@SP
M=D+1
@R12
D=M-1
AM=D
D=M
@THAT
M=D
@R12
D=M-1
AM=D
D=M
@THIS
M=D
@R12
D=M-1
AM=D
D=M
@ARG
M=D
@R12
D=M-1
AM=D
D=M
@LCL
M=D
@R14
A=M
0;JMP
//function Class2.set 0
(Class2.set)
//push argument 0
@0
D=A
@ARG
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
//pop static 0
@SP
M=M-1
A=M
D=M
@Class2-0
M=D
//push argument 1
@1
D=A
@ARG
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
//pop static 1
@SP
M=M-1
A=M
D=M
@Class2-1
M=D
//push constant 0
@0
D=A
@SP
A=M
M=D
@SP
M=M+1
//return
@LCL
D=M
@R12
M=D
@5
D=D-A
A=D
D=M
@R14
M=D
@ARG
D=M
@0
D=D+A
@R13
M=D
@SP
AM=M-1
D=M
@R13
A=M
M=D
@ARG
D=M
@SP
M=D+1
@R12
D=M-1
AM=D
D=M
@THAT
M=D
@R12
D=M-1
AM=D
D=M
@THIS
M=D
@R12
D=M-1
AM=D
D=M
@ARG
M=D
@R12
D=M-1
AM=D
D=M
@LCL
M=D
@R14
A=M
0;JMP
//function Class2.get 0
(Class2.get)
//push static 0
@Class2-0
D=M
@SP
A=M
M=D
@SP
M=M+1
//push static 1
@Class2-1
D=M
@SP
A=M
M=D
@SP
M=M+1
//sub
@SP
AM=M-1
D=M
A=A-1
M=M-D
//return
@LCL
D=M
@R12
M=D
@5
D=D-A
A=D
D=M
@R14
M=D
@ARG
D=M
@0
D=D+A
@R13
M=D
@SP
AM=M-1
D=M
@R13
A=M
M=D
@ARG
D=M
@SP
M=D+1
@R12
D=M-1
AM=D
D=M
@THAT
M=D
@R12
D=M-1
AM=D
D=M
@THIS
M=D
@R12
D=M-1
AM=D
D=M
@ARG
M=D
@R12
D=M-1
AM=D
D=M
@LCL
M=D
@R14
A=M
0;JMP
//function Sys.init 0
(Sys.init)
//push constant 6
@6
D=A
@SP
A=M
M=D
@SP
M=M+1
//push constant 8
@8
D=A
@SP
A=M
M=D
@SP
M=M+1
//call Class1.set 2
@RETURN_ADDR1
D=A
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
@2
D=A
@5
D=D+A
@SP
D=M-D
@ARG
M=D
@SP
D=M
@LCL
M=D
@Class1.set
0;JMP
(RETURN_ADDR1)
//pop temp 0
@0
D=A
@R5
D=A+D
@R13
M=D
@SP
AM=M-1
D=M
@R13
A=M
M=D
//push constant 23
@23
D=A
@SP
A=M
M=D
@SP
M=M+1
//push constant 15
@15
D=A
@SP
A=M
M=D
@SP
M=M+1
//call Class2.set 2
@RETURN_ADDR2
D=A
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
@2
D=A
@5
D=D+A
@SP
D=M-D
@ARG
M=D
@SP
D=M
@LCL
M=D
@Class2.set
0;JMP
(RETURN_ADDR2)
//pop temp 0
@0
D=A
@R5
D=A+D
@R13
M=D
@SP
AM=M-1
D=M
@R13
A=M
M=D
//call Class1.get 0
@RETURN_ADDR3
D=A
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@5
D=D+A
@SP
D=M-D
@ARG
M=D
@SP
D=M
@LCL
M=D
@Class1.get
0;JMP
(RETURN_ADDR3)
//call Class2.get 0
@RETURN_ADDR4
D=A
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@5
D=D+A
@SP
D=M-D
@ARG
M=D
@SP
D=M
@LCL
M=D
@Class2.get
0;JMP
(RETURN_ADDR4)
//label WHILE
(WHILE)
//goto WHILE
@WHILE
0;JMP
