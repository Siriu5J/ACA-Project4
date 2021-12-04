Begin Assembly
ADDI R1, R0, 4000
ADDI R2, R0, 4004
ADDI R10, R0, 12
Label jump
LW R3, 0(R1)
LW R4, 0(R2)
ADD R5, R3, R4
SW R5, 4(R2)
BGEZ R5, jump
ADD R1, R3, R2
HALT

End Assembly
-- begin main data
Begin Data 4000 44
6
3
0
End Data
-- stack
Begin Data 5000 100
End Data