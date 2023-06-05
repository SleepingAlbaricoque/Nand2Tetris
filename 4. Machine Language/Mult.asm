// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
//
// This program only needs to handle arguments that satisfy
// R0 >= 0, R1 >= 0, and R0*R1 < 32768.

// Add i n times while decrement n by 1
// i = RAM[0]
// n = RAM[1]
// k = RAM[2]

// LOOP:
//     if n == 0 goto END
//     n = n - 1
//     k = k + i
//     goto LOOP

// END:
//     goto END

(LOOP)
     @R1
     D=M // n = RAM[1]

     @END
     D;JLE // if n <= 0 goto END

     @R1
     M=M-1 // n = n - 1

     @R0
     D=M // i = RAM[0]

     @R2
     M=M+D // k = k + i

     @LOOP
     0;JMP

(END)
     @END
     0;JMP
     

