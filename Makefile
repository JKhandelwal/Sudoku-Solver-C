CC = clang -g
CFLAGS = -Wall -Wextra -std=c99 	# for c compiler
LFLAGS = -Wall -Wextra	# for object code creation/linker

all: sudoku_check sudoku_solver sudoku_advanced

clean:
	rm -f *.o sudoku_check sudoku_solver sudoku_advanced

sudoku_check: sudoku_check.o sudoku_io.o sudoku_functions.o
	${CC} ${LFLAGS} sudoku_check.o sudoku_io.o sudoku_functions.o -o sudoku_check

sudoku_check.o: sudoku_check.c header.h 
	${CC} ${CFLAGS} sudoku_check.c  -c -o sudoku_check.o

sudoku_solver: sudoku_solver.o sudoku_io.o sudoku_functions.o
	${CC} ${LFLAGS} sudoku_solver.o sudoku_io.o sudoku_functions.o -o sudoku_solver

sudoku_solver.o: sudoku_solver.c header.h 
	${CC} ${CFLAGS}  sudoku_solver.c  -c -o sudoku_solver.o

sudoku_advanced: sudoku_advanced.o sudoku_io.o  sudoku_functions.o
	${CC} ${LFLAGS} -O3 sudoku_advanced.o sudoku_io.o sudoku_functions.o -o sudoku_advanced

sudoku_advanced.o: sudoku_advanced.c header.h
	${CC} ${CFLAGS} sudoku_advanced.c  -c -o sudoku_advanced.o

sudoku_functions.o: sudoku_functions.c header.h 
	${CC} ${CFLAGS} sudoku_functions.c  -c -o sudoku_functions.o

sudoku_io.o: sudoku_io.c header.h 
	${CC} ${CFLAGS} sudoku_io.c -c -o sudoku_io.o
