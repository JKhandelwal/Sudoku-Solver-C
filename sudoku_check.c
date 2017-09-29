#include "header.h"

//Reads in the sudoku and stores it in the array.
int main(void)
{
    int n;
    int temp;
    scanf("%d", &n);
    int bound = n*n;
    sudoku c; 
    c.array = malloc(sizeof(int) * (bound * bound));
    c.size = n;
    int counter = 0;
    for (int i = 0; i < bound; i++)
    {
        for (int j = 0; j < bound; j++)
        {
            scanf("%d", &temp);
            c.array[counter] = temp;
            counter++;
        }
    }
    print_Sudoku(c);
    retVal_type t = check_sudoku(c);
    if (t == COMPLETE) 
    {
        printf("COMPLETE\n");
    } 
    else if (t == INVALID)
    {
        printf("INVALID\n");
    } 
    else 
    {
        printf("INCOMPLETE\n");
    }
    free_Sudoku(c);
}

