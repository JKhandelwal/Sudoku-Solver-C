#include "header.h"
void solve_Sudoku(sudoku c);
int no_of_solutions_found = 0;
bool solvable = false;
sudoku solved;
sudoku initial;

int main(void)
{
    //Reads in the sudoku and stores it in the array.
    int n;
    int temp;
    scanf("%d", &n);
    int bound = n*n;
    initial.array = malloc(sizeof(int) * (bound * bound));
    initial.size = n;
    int counter = 0;
    for (int i = 0; i < bound; i++)
    {
        for (int j = 0; j < bound; j++)
        {
            scanf("%d", &temp);
            initial.array[counter] = temp;
            counter++;
        }
    }

     solve_Sudoku(initial);
     if (solvable){
         print_Sudoku(solved);
         free_Sudoku(solved);
     } else {
        printf("UNSOLVABLE\n");
     }
    free_Sudoku(initial);

}
int counter2 = 0;

void solve_Sudoku(sudoku c){
    retVal_type t = check_sudoku(c);
    ++counter2;
    if (t == INVALID){
        return;
    }

//Deals with complete and multiple solutions
    if (t == COMPLETE){
        if (no_of_solutions_found  == 0 ){
            solved = copy_Sudoku(c.array,c.size);
            solvable = true;
            no_of_solutions_found++;
            return;
        } else {
            free_Sudoku(solved);
            free_Sudoku(initial);
            free_Sudoku(c);
            printf("MULTIPLE\n");
            exit(0);
        }
    }

    sudoku s = copy_Sudoku(c.array, c.size);
    for (int i =0; i < (POW4(c.size)); i++){
        if (c.array[i] == 0){
            for (int j = 1; j < (c.size * c.size +1); j++){
                s.array[i] = j;
                solve_Sudoku(s);
            }
            break;
        }
    }
    free_Sudoku(s);
}
