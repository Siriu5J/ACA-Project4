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
0:  J QSTest
      ------------------------------------------
      -- Start quickSort
      ------------------------------------------
LABEL quickSort
      -- For each partition in the recursion, add return address and hi value to the stack
4:  ADDI R3, R3, 4
      -- Store the jump return address in the stack
8:  SW R31, 0(R3)
      -- Compute low minus high, we will skip the qs stuff if this is >= 0 
12:  SUB R6, R15, R16
      -- Branch to QSIF if R6 is >=0
16:  BGEZ R6, QSIF
      -- Run the Partition function
20:  JAL Partition
      -- increment the stack pointer to store the partitioning index value
24:  ADDI R3, R3, 4
      -- Store the partitioning index (pi) value
28:  SW R21, 0(R3)
      -- increment the stack pointer to store the high value
32:  ADDI R3, R3, 4
      -- Store the high value
36:  SW R16, 0(R3)
      -- Next we are going to run the quicksort function between low and pi - 1
      -- calculate pi - 1 and put it in the "high" Register
40:  ADDI R16, R21, -1
      -- Run quicksort for the bottom part
44:  JAL quickSort
      -- I need to get the original high value
48:  LW R16, 0(R3)
      -- Since i popped it off the stack, i need to decrement the stack pointer
52:  ADDI R3, R3, -4
      -- Now I need the original pi
56:  LW R21, 0(R3)
      -- same decrementation
60:  ADDI R3, R3, -4
      -- I need to load the pi+1 into low
64:  ADDI R15, R21, 1
      -- Run quickSort for the top part
68:  JAL quickSort
LABEL QSIF
      -- After you have completed the quicksort, pop it off the stack and return to the place it was called
72:  LW R31, 0(R3)
76:  ADDI R3, R3, -4
80:  JR R31
      -- End quickSort
      -------------------------------------------
      -- Test quickSort
      -------------------------------------------
LABEL QSTest
      -- initiate the stack
84:  ADDI R3, R0, 5000
      -- Arr is at Org 4000
88:  ADDI R14, R0, 4000
      -- low is 0
92:  ADDI R15, R0, 0
      -- high is 10
96:  ADDI R16, R0, 4
100:  JAL quickSort
104:  HALT
      ------------------------------------------
      -- Test partition
      ------------------------------------------
LABEL PartitionTest
      -- Arr is at Org 4000
108:  ADDI R14, R0, 4000
      -- low is 0
112:  ADDI R15, R0, 0
      -- high is 10
116:  ADDI R16, R0, 4
120:  JAL Partition
124:  HALT
      ------------------------------------------
      -- Test swap
      ------------------------------------------
LABEL SwapTest
      -- Load 4012 as the address A in swap
128:  ADDI R10, R0, 4016
      -- Load 4016 as the address B in swap
132:  ADDI R11, R0, 4020
136:  JAL Swap
140:  HALT
      -------------------------------------------
      -- Start Swap
      -------------------------------------------
LABEL Swap
      -- Pull in data from A address to R12
144:  LW R12, 0(R10)
      -- Pull in data from B address to R13
148:  LW R13, 0(R11)
      -- Write R12 to 0(R11)
152:  SW R12, 0(R11)
      -- Write R13 to 0(R10)
156:  SW R13, 0(R10)
160:  JR R31
      -- End Swap
      --------------------------------------------
      --start Partition
      --------------------------------------------
LABEL Partition
      -- send jump address to memory location 5000
164:  ADDI R2, R0, 5000
168:  SW R31, 0(R2)
      -- get the high value memory offset
172:  SLL R9, R16, 2
      -- get the arr[high] memory location
176:  ADD R9, R9, R14
      --ADDI R9, R9, -4
      -- get the correct pivot value
180:  LW R17, 0(R9)
      --set i = low-1
184:  ADDI R18, R15, -1
      -- set the high-1 Register
188:  ADDI R20, R16, -1
      -- initialize j
192:  ADD R19, R15, R0
LABEL StartPartitionLoop
      -- Get j memory offset
196:  SLL R9, R19, 2
      --get arr[j] memory location
200:  ADD R9, R9, R14
      -- get the arr[j] value
204:  LW R8, 0(R9)
      -- subtract pivot from arr[j] then the equivalent to the if
      -- statement will be that value BLEZ
208:  SUB R7, R8, R17
212:  BGTZ R7, PostPartitionLoop
      -- increment i by 1
216:  ADDI R18, R18, 1
      -- load the swap Registers A first then B
220:  ADD R10, R9, R0
      -- to get the address of the current arr[i] i need to multiply i and add
224:  SLL R9, R18, 2
228:  ADD R9, R9, R14
      -- Finally load B
232:  ADD R11, R9, R0
236:  JAL Swap
LABEL PostPartitionLoop
      -- get the BLEZ value for j-(high-1)
240:  SUB R9, R19, R20
      -- increment j
244:  ADDI R19, R19, 1
      -- Do the loop branch
248:  BLTZ R9, StartPartitionLoop
      --if i get here then the loop has ended so I need to do those last 
      --two things
      --load the swap data A=(&arr[i+1]), B=(&arr[high])
      -- increment i, multiply by 4, add to array address base
252:  ADDI R18, R18, 1
256:  SLL R9, R18, 2
260:  ADD R10, R9, R14
      -- set the A Register
      --ADD R10, R9, R0
      --multiply high by 4, then add to array base address
264:  SLL R9, R16, 2
268:  ADD R11, R9, R14
      -- set the B Register
      --ADD R11, R9, R0
      -- swap A and B
272:  JAL Swap
      -- set the partition return value
276:  ADD R21, R18, R0
280:  LW R31, 0(R2)
284:  JR R31
      --End partition
