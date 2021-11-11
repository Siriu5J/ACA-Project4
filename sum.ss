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
      -- Stack will be at Org5000 - R30 is SP
0:  ADDI R30, R0, 5000
      -- Data is at Org 4000
4:  ADDI R4, R0, 4000
      -- Load number of elements
8:  LW R2, 0(R4)
      -- Multiply this by 4, since each element is 4 bytes
12:  SLL R3, R2, 2
      -- R4 is address of beginning of array of numbers
16:  ADDI R4, R4, 8
      -- R5 now points to first address past array
20:  ADD R5, R4, R3
      -- initialize loop variable to first address (4008)
24:  ADD R6, R4, R0
      -- sum = 0
28:  ADD R7, R0, R0
LABEL LoopStart
32:  BEQ R6, R5, PostLoop
      -- load current value
36:  LW R8, 0(R6)
      -- pass parameters (curr value and curr sum)
40:  ADD R20, R8, R0
44:  ADD R21, R7, R0
48:  JAL AddThem
      -- move sum from return reg to R7
52:  ADD R7, R1, R0
      -- increment address (by 4 bytes)
56:  ADDI R6, R6, 4
60:  J LoopStart
LABEL PostLoop
      -- store answer
64:  SW R7, -4(R4)
68:  HALT
      -- subroutine to add 2 numbers
LABEL AddThem
      -- if doing recursion, must save R31
72:  SW R31, 0(R30)
      -- post incr the SP
76:  ADDI R30, R30, 4
      -- Since subroutine uses R5, must save
80:  SW R5, 0(R30)
84:  ADDI R30, R30, 4
      -- get nums from parameter regs and sum
88:  ADD R5, R20, R21
      -- move result to return reg
92:  ADD R1, R5, R0
      -- now put stack back the way it was
      -- and restore return address and R5
96:  ADDI R30, R30, -4
100:  LW R5, 0(R30)
104:  ADDI R30, R30, -4
108:  LW R31, 0(R30)
112:  NOP
      -- return from subroutine
116:  JR R31
120:  NOP
