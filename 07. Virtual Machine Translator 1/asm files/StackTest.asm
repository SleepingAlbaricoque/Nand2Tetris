//push constant 17
@17
D=A
@SP
A=M
M=D
@SP
M=M+1
//push constant 17
@17
D=A
@SP
A=M
M=D
@SP
M=M+1
// eq
@SP
AM=M-1
D=M
A=A-1
D=M-D
@TRUE0
D;JEQ
@SP
A=M-1
M=0
@CONTINUE0
0;JMP
(TRUE0)
@SP
A=M-1
M=-1
(CONTINUE0)
//push constant 17
@17
D=A
@SP
A=M
M=D
@SP
M=M+1
//push constant 16
@16
D=A
@SP
A=M
M=D
@SP
M=M+1
// eq
@SP
AM=M-1
D=M
A=A-1
D=M-D
@TRUE1
D;JEQ
@SP
A=M-1
M=0
@CONTINUE1
0;JMP
(TRUE1)
@SP
A=M-1
M=-1
(CONTINUE1)
//push constant 16
@16
D=A
@SP
A=M
M=D
@SP
M=M+1
//push constant 17
@17
D=A
@SP
A=M
M=D
@SP
M=M+1
// eq
@SP
AM=M-1
D=M
A=A-1
D=M-D
@TRUE2
D;JEQ
@SP
A=M-1
M=0
@CONTINUE2
0;JMP
(TRUE2)
@SP
A=M-1
M=-1
(CONTINUE2)
//push constant 892
@892
D=A
@SP
A=M
M=D
@SP
M=M+1
//push constant 891
@891
D=A
@SP
A=M
M=D
@SP
M=M+1
// lt
@SP
AM=M-1
D=M
A=A-1
D=M-D
@TRUE3
D;JLT
@SP
A=M-1
M=0
@CONTINUE3
0;JMP
(TRUE3)
@SP
A=M-1
M=-1
(CONTINUE3)
//push constant 891
@891
D=A
@SP
A=M
M=D
@SP
M=M+1
//push constant 892
@892
D=A
@SP
A=M
M=D
@SP
M=M+1
// lt
@SP
AM=M-1
D=M
A=A-1
D=M-D
@TRUE4
D;JLT
@SP
A=M-1
M=0
@CONTINUE4
0;JMP
(TRUE4)
@SP
A=M-1
M=-1
(CONTINUE4)
//push constant 891
@891
D=A
@SP
A=M
M=D
@SP
M=M+1
//push constant 891
@891
D=A
@SP
A=M
M=D
@SP
M=M+1
// lt
@SP
AM=M-1
D=M
A=A-1
D=M-D
@TRUE5
D;JLT
@SP
A=M-1
M=0
@CONTINUE5
0;JMP
(TRUE5)
@SP
A=M-1
M=-1
(CONTINUE5)
//push constant 32767
@32767
D=A
@SP
A=M
M=D
@SP
M=M+1
//push constant 32766
@32766
D=A
@SP
A=M
M=D
@SP
M=M+1
// gt
@SP
AM=M-1
D=M
A=A-1
D=M-D
@TRUE6
D;JGT
@SP
A=M-1
M=0
@CONTINUE6
0;JMP
(TRUE6)
@SP
A=M-1
M=-1
(CONTINUE6)
//push constant 32766
@32766
D=A
@SP
A=M
M=D
@SP
M=M+1
//push constant 32767
@32767
D=A
@SP
A=M
M=D
@SP
M=M+1
// gt
@SP
AM=M-1
D=M
A=A-1
D=M-D
@TRUE7
D;JGT
@SP
A=M-1
M=0
@CONTINUE7
0;JMP
(TRUE7)
@SP
A=M-1
M=-1
(CONTINUE7)
//push constant 32766
@32766
D=A
@SP
A=M
M=D
@SP
M=M+1
//push constant 32766
@32766
D=A
@SP
A=M
M=D
@SP
M=M+1
// gt
@SP
AM=M-1
D=M
A=A-1
D=M-D
@TRUE8
D;JGT
@SP
A=M-1
M=0
@CONTINUE8
0;JMP
(TRUE8)
@SP
A=M-1
M=-1
(CONTINUE8)
//push constant 57
@57
D=A
@SP
A=M
M=D
@SP
M=M+1
//push constant 31
@31
D=A
@SP
A=M
M=D
@SP
M=M+1
//push constant 53
@53
D=A
@SP
A=M
M=D
@SP
M=M+1
// add
@SP
AM=M-1
D=M
A=A-1
M=M+D
//push constant 112
@112
D=A
@SP
A=M
M=D
@SP
M=M+1
// sub
@SP
AM=M-1
D=M
A=A-1
M=M-D
// neg
@0
D=A
A=M-1
M=D-M
// and
@SP
AM=M-1
D=M
A=A-1
M=M&D
//push constant 82
@82
D=A
@SP
A=M
M=D
@SP
M=M+1
// or
@SP
AM=M-1
D=M
A=A-1
M=M|D
// not
@SP
A=M-1
M=!M
