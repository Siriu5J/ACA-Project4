Begin Assembly
Label jump
ADDI R1, R0, 100
BLTZ R1, jump
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