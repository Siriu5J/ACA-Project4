-- Registers
-- R2 is not always but for testing purposes, partition ret addr
-- R3 the spot in the stack where the return address for qs is
-- R6 QS GPR
-- R7 Partition GPR
-- R8 Partition GPR
-- R9 Partition GPR
-- R10 is swap A address
-- R11 is swap B address
-- R12 is temp for swap
-- R13 is temp2 for swap
-- R14 is Array sp
-- R15 is low value
-- R16 is high value
-- R17 is pivot
-- R18 is i
-- R19 is j
-- R20 is high-1 in partition
-- R21 is partition return value (also the partitioning index(pi))
-- R31 is the hardwares return address pointer
Begin Assembly
J PartitionTest
------------------------------------------
-- Start quickSort
------------------------------------------
LABEL quickSort
-- For each partition in the recursion, add return address and hi value to the stack
ADDI R3, R3, 4
-- Store the jump return address in the stack
SW R31, 0(R3)
-- Compute low minus high, we will skip the qs stuff if this is >= 0 
SUB R6, R15, R16
-- Branch to QSIF if R6 is >=0
BGEZ R6, QSIF
-- Run the Partition function
JAL Partition
-- increment the stack pointer to store the partitioning index value
ADDI R3, R3, 4
-- Store the partitioning index (pi) value
SW R21, 0(R3)
-- increment the stack pointer to store the high value
ADDI R3, R3, 4
-- Store the high value
SW R16, 0(R3)
-- Next we are going to run the quicksort function between low and pi - 1
-- calculate pi - 1 and put it in the "high" Register
ADDI R16, R21, -1
-- Run quicksort for the bottom part
JAL quickSort
-- I need to get the original high value
LW R16, 0(R3)
-- Since i popped it off the stack, i need to decrement the stack pointer
ADDI R3, R3, -4
-- Now I need the original pi
LW R21, 0(R3)
-- same decrementation
ADDI R3, R3, -4
-- I need to load the pi+1 into low
ADDI R15, R21, 1
-- Run quickSort for the top part
JAL quickSort
LABEL QSIF
-- After you have completed the quicksort, pop it off the stack and return to the place it was called
LW R31, 0(R3)
ADDI R3, R3, -4
JR R31
-- End quickSort
-------------------------------------------
-- Test quickSort
-------------------------------------------
LABEL QSTest
-- initiate the stack
ADDI R3, R0, 5000
-- Arr is at Org 4000
ADDI R14, R0, 4000
-- low is 0
ADDI R15, R0, 0
-- high is 10
ADDI R16, R0, 4
JAL quickSort
HALT
------------------------------------------
-- Test partition
------------------------------------------
LABEL PartitionTest
-- Arr is at Org 4000
ADDI R14, R0, 4000
-- low is 0
ADDI R15, R0, 0
-- high is 10
ADDI R16, R0, 4
JAL Partition
HALT
------------------------------------------
-- Test swap
------------------------------------------
LABEL SwapTest
-- Load 4012 as the address A in swap
ADDI R10, R0, 4016
-- Load 4016 as the address B in swap
ADDI R11, R0, 4020
JAL Swap
HALT
-------------------------------------------
-- Start Swap
-------------------------------------------
LABEL Swap
-- Pull in data from A address to R12
LW R12, 0(R10)
-- Pull in data from B address to R13
LW R13, 0(R11)
-- Write R12 to 0(R11)
SW R12, 0(R11)
-- Write R13 to 0(R10)
SW R13, 0(R10)
JR R31
-- End Swap
--------------------------------------------
--start Partition
--------------------------------------------
LABEL Partition
-- send jump address to memory location 5000
ADDI R2, R0, 5000
SW R31, 0(R2)
-- get the high value memory offset
SLL R9, R16, 2
-- get the arr[high] memory location
ADD R9, R9, R14
--ADDI R9, R9, -4
-- get the correct pivot value
LW R17, 0(R9)
--set i = low-1
ADDI R18, R15, -1
-- set the high-1 Register
ADDI R20, R16, -1
-- initialize j
ADD R19, R15, R0
LABEL StartPartitionLoop
-- Get j memory offset
SLL R9, R19, 2
--get arr[j] memory location
ADD R9, R9, R14
-- get the arr[j] value
LW R8, 0(R9)
-- subtract pivot from arr[j] then the equivalent to the if
-- statement will be that value BLEZ
SUB R7, R8, R17
BGTZ R7, PostPartitionLoop
-- increment i by 1
ADDI R18, R18, 1
-- load the swap Registers A first then B
ADD R10, R9, R0
-- to get the address of the current arr[i] i need to multiply i and add
SLL R9, R18, 2
ADD R9, R9, R14
-- Finally load B
ADD R11, R9, R0
JAL Swap
LABEL PostPartitionLoop
-- get the BLEZ value for j-(high-1)
SUB R9, R19, R20
-- increment j
ADDI R19, R19, 1
-- Do the loop branch
BLTZ R9, StartPartitionLoop
--if i get here then the loop has ended so I need to do those last 
--two things
--load the swap data A=(&arr[i+1]), B=(&arr[high])
-- increment i, multiply by 4, add to array address base
ADDI R18, R18, 1
SLL R9, R18, 2
ADD R10, R9, R14
-- set the A Register
--ADD R10, R9, R0
--multiply high by 4, then add to array base address
SLL R9, R16, 2
ADD R11, R9, R14
-- set the B Register
--ADD R11, R9, R0
-- swap A and B
JAL Swap
-- set the partition return value
ADD R21, R18, R0
LW R31, 0(R2)
JR R31
--End partition
End Assembly
-- begin main data
Begin Data 4000 600
20
83
71
14
30
End Data
Begin Data 3000 200
End Data