--This program presets an array and sorts it using bubble sort
--4000 = number of elements in array
--4004 = address of first array element
--
--R1 = Temp for j+1
--R2 = Counter for outer loop
--R3 = Counter for innter loop
--R4 = Number of elements
--R5 = Address of first element
--R6 = 1st num for compare
--R7 = 2nd num for compare
--R8 = Address of number of elements
--R10= Conditional compare storage
--R11= Temp Swap
--R26= Outer loop limit
--R27= Inner loop limit
--
Begin Assembly
--Setup register
ADDI R5, R0, 4004
ADDI R8, R0, 4000
LW R4, 0(R8)
--
--Setup outer loop
SLL R26, R4, 2 --limit = multiply number of elements by 4
ADDI R26, R26, -4 --limit minus 4
ADDI R2, R0, 0 --init i
--Start outer loop
LABEL OuterLoop
BEQ R2, R26, PostOuterLoop
--
--Setup inner loop
SLL R27, R4, 2 --limit = multiply number of elements by 4
ADD R27, R27, R5 --Add the address of the first element to limit
SUB R27, R27, R2 --Subtract i from limit
ADDI R27, R27, -4 --j -= 4
ADD R3, R0, R5 --Initialize counter to first element
LABEL InnerLoop
BEQ R3, R27, PostInnerLoop --j == limit then branch
LW R6, 0(R3)
ADDI R1, R3, 4 --temp for i+4
LW R7, 0(R1)
--Condition R6 > R7
SUB R10, R6, R7 --R6 - R7
BLTZ R10, NoSwap
--Swap
ADD R11, R0, R7
ADD R7, R0, R6
ADD R6, R0, R11
SW R6, 0(R3)
SW R7, 0(R1)
--NoSwap
LABEL NoSwap
--
ADDI R3, R3, 4 --j += 4
J InnerLoop
--End inner loop
LABEL PostInnerLoop
ADDI R2, R2, 4 --i += 4
J OuterLoop
--End outer loop
LABEL PostOuterLoop
HALT
End Assembly
--Data Stuff
Begin Data 4000 600
20
415
885
432
98
123
62
758
458
684
501
281
432
403
34
346
485
531
903
964
609
End Data