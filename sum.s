--A program to find the sum of a list of numbers
-- The program uses a subroutine to add 2 numbers, as a demo
-- It also sets up a stack frame, although not needed for this program
-- 4000 = # of nums to sum
-- 4004  = location for sum to be put
-- 4008 = beginning of array of nums
-- 
-- R20, R21 - parameter passing regs
-- R30 = SP
-- R31 = Ret Addr Reg
-- R3 = size of array, in bytes
-- R4 = Address of beginning of array (4008)
-- R5 = first address past array, for loop termination
-- R6 = current address being worked on (loop i variable)
-- R7 = sum
-- R8 = current array data value
-- 
Begin Assembly
-- Stack will be at Org5000 - R30 is SP
ADDI R30, R0, 5000
-- Data is at Org 4000
ADDI R4, R0, 4000
-- Load number of elements
LW R2, 0(R4)
-- Multiply this by 4, since each element is 4 bytes
SLL R3, R2, 2
-- R4 is address of beginning of array of numbers
ADDI R4, R4, 8
-- R5 now points to first address past array
ADD R5, R4, R3
-- initialize loop variable to first address (4008)
ADD R6, R4, R0
-- sum = 0
ADD R7, R0, R0
LABEL LoopStart
BEQ R6, R5, PostLoop
-- load current value
LW R8, 0(R6)
-- pass parameters (curr value and curr sum)
ADD R20, R8, R0
ADD R21, R7, R0
JAL AddThem
-- move sum from return reg to R7
ADD R7, R1, R0
-- increment address (by 4 bytes)
ADDI R6, R6, 4
J LoopStart
LABEL PostLoop
-- store answer
SW R7, -4(R4)
HALT
-- subroutine to add 2 numbers
LABEL AddThem
-- if doing recursion, must save R31
SW R31, 0(R30)
-- post incr the SP
ADDI R30, R30, 4
-- Since subroutine uses R5, must save
SW R5, 0(R30)
ADDI R30, R30, 4
-- get nums from parameter regs and sum
ADD R5, R20, R21
-- move result to return reg
ADD R1, R5, R0
-- now put stack back the way it was
-- and restore return address and R5
ADDI R30, R30, -4
LW R5, 0(R30)
ADDI R30, R30, -4
LW R31, 0(R30)
NOP
-- return from subroutine
JR R31
NOP
End Assembly
-- begin main data
Begin Data 4000 44
10
0
23
71
33
5
93
82
34
13
111
23
End Data
-- stack
Begin Data 5000 100
End Data