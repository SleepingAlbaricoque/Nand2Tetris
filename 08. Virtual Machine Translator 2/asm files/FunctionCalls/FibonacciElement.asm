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
//function Main.fibonacci 0
(Main.fibonacci)
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
//push constant 2
@2
D=A
@SP
A=M
M=D
@SP
M=M+1
//lt
@SP
AM=M-1
D=M
A=A-1
D=M-D
@TRUE0
D;JLT
@SP
A=M-1
M=0
D=0
@CONTINUE0
0;JMP
(TRUE0)
@SP
A=M-1
M=-1
(CONTINUE0)
//if-goto IF_TRUE
@IF_TRUE
D;JNE
//goto IF_FALSE
@IF_FALSE
0;JMP
//label IF_TRUE
(IF_TRUE)
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
//label IF_FALSE
(IF_FALSE)
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
//push constant 2
@2
D=A
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
//call Main.fibonacci 1
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
@1
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
@Main.fibonacci
0;JMP
(RETURN_ADDR1)
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
//push constant 1
@1
D=A
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
//call Main.fibonacci 1
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
@1
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
@Main.fibonacci
0;JMP
(RETURN_ADDR2)
//add
@SP
AM=M-1
D=M
A=A-1
M=M+D
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
//push constant 4
@4
D=A
@SP
A=M
M=D
@SP
M=M+1
//call Main.fibonacci 1
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
@1
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
@Main.fibonacci
0;JMP
(RETURN_ADDR3)
//label WHILE
(WHILE)
//goto WHILE
@WHILE
0;JMP
