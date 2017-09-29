#include "header.h"

void free_Sudoku(sudoku s)
{
    free(s.array);
}

sudoku copy_Sudoku(int* sudoku_to_copy, int n)
{
 sudoku c;
 c.size = n;
 c.array = malloc(sizeof(int) * POW4(n));
 c.array = memmove(c.array, sudoku_to_copy, sizeof(int) * POW4(n));
 return c;
}

void print_Sudoku(sudoku c)
{
    int counter = 0;
    for (int i=0; i < POW4(c.size);i++)
    {
        if (counter++ == ((c.size * c.size)-1)){
            printf("%3d\n", c.array[i]);
            counter = 0;
        } else {
           printf("%3d", c.array[i]);
        }
    }
}