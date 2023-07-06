// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.


// k = @KEB 
// i = a pointer to keep track of NOTZERO and ZERO loops
// n = 8191  // the number of rows in the screen memory map
//
// LOOP: 
//     check the keyboard input value
//     if k > 0 goto NOT ZERO
//     if k = 0 goto ZERO
//
// NOT ZERO:     
//      switch every row's value to 1
//      goto LOOP
//
// ZERO:
//      switch every row's value to 0
//      goto LOOP



(LOOP)
     @SCREEN // this segment should be inside the loop so whenever the k value changes, the address value changes as well
     D=A
     @addr
     M=D // addr = screen's base address, which is 16384
     @8191
     D=A
     @n
     M=D // the number of rows in the screen memory map

     @KBD
     D=M
     @NOTZERO
     D;JNE // if k != 0 goto NOTZERO
     @ZERO
     D;JEQ // if k == 0 goto ZERO

(NOTZERO)
     @n
     D=M
     @LOOP
     D;JLT // if n < 0 goto LOOP

     @addr
     A=M
     M = -1 // RAM[addr] = 1111111111111111

     @n
     M = M -1 // n = n - 1
     @32
     D=A // the value we need to skip to the next row in the screen memory map
     @addr
     M=M+1 // addr = addr + 1
     @NOTZERO
     0;JMP

(ZERO)
     @n
     D=M
     @LOOP
     D;JLT // if n < 0 goto LOOP

     @addr
     A=M
     M = 0 // RAM[addr] = 0000000000000000

     @n
     M = M -1 // n = n - 1
     @32
     D=A // the value we need to skip to the next row in the screen memory map
     @addr
     M=M+1 // addr = addr + 1
     @ZERO
     0;JMP
     