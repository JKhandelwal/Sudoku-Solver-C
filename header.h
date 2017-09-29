#ifndef SUDOKU_HEADER_FILES_CS2002_PRACTICAL
#define SUDOKU_HEADER_FILES_CS2002_PRACTICAL
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
typedef struct 
{
    int* array;
    int size;
} sudoku;

typedef enum
{
    INVALID, 
    INCOMPLETE,
    COMPLETE,
} retVal_type;


void print_Sudoku(sudoku s);
void free_Sudoku(sudoku array);
sudoku copy_Sudoku(int* array, int size);
retVal_type check_sudoku(sudoku c);

retVal_type check_list(int* list, int length);
#define POW4(x) ((x)*(x)*(x)*(x))

#endif