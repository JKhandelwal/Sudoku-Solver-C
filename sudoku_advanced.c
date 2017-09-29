#include "header.h"

int no_of_solutions_found = 0;
bool solvable = false;
sudoku solved;
sudoku initial;
//Keep track variables
int numZeros = 0;
int column;
int row;
int box_corner;
int firstCounter =0;

void solve_Sudoku(sudoku c, int numZeros);

int is_Addition_Valid(sudoku c);
//Reads in the sudoku and stores it in the array.
int main(void) {

    int n;
    int temp;
    scanf("%d", &n);
    int bound = n * n;
    initial.array = malloc(sizeof(int) * (bound * bound));
    initial.size = n;
    int counter = 0;
    for (int i = 0; i < bound; i++) {
        for (int j = 0; j < bound; j++) {
            scanf("%d", &temp);
            //Count the number of zeros in the code 
            if (temp == 0) {
                numZeros++;
            }
            initial.array[counter] = temp;
            counter++;
        }
    }

    solve_Sudoku(initial, numZeros);

    if (solvable) {
        print_Sudoku(solved);
        free_Sudoku(solved);
    } else {
        printf("UNSOLVABLE\n");
    }
    free_Sudoku(initial);

}

void solve_Sudoku(sudoku c, int numZeros) {
    //Does the check for the sudoku the first time the function is called. 
    if (firstCounter ==0){
        retVal_type t = check_sudoku(c);
        if (t == INVALID) {
            return;
        } else {
            if (t == COMPLETE) {
                solvable = true;
                solved = copy_Sudoku(c.array,c.size);
                return;
            }
        }
        firstCounter++;
    }
//Only gets called if a solution is passed in i.e. when the number of zeros in the solution is 0
        if (numZeros == 0) {
                if (no_of_solutions_found == 0) {
                    solved = copy_Sudoku(c.array, c.size);
                    solvable = true;
                    no_of_solutions_found++;
                    return;
                } else {
                    free_Sudoku(solved);
                    free_Sudoku(initial);
                    printf("MULTIPLE\n");
                    exit(0);
                }
        }

    for (int i = 0; i < (POW4(c.size)); i++) {
        if (c.array[i] == 0) {
            for (int j = 1; j < (c.size * c.size + 1); j++) {
                //Set all of the variables for this iteration of the loop
                int numTempZeros = numZeros--;
                column = i % (c.size * c.size);
                row = i / (c.size * c.size);
                int box_row = (row / c.size) % c.size;
                int box_clm = (column / c.size) % c.size;
                box_corner = box_row * c.size * c.size * c.size + box_clm * c.size;
                int temp = c.array[i];
                c.array[i] = j;
                //check if the number is valid in that square 
                if (is_Addition_Valid(c)){
                    solve_Sudoku(c, numZeros);
                }
                //reverts the values to what they were before the call for the backtracking. 
                c.array[i] = temp;
                numZeros = numTempZeros;
            }
            break;
        }
    }


}


int is_Addition_Valid(sudoku c) {
    retVal_type t;
    int *startp;
    int temp[c.size * c.size];

    //check row
    startp = &c.array[c.size * c.size * row];
    t = check_list(startp, c.size * c.size);
    if (t == INVALID) {
        return 0;
    } 

    //check column
    for (int j = 0; j < (c.size * c.size); j++) {
        temp[j] = c.array[column + j * (c.size * c.size)];
    }
    startp = temp;
    t = check_list(startp, c.size * c.size);
    if (t == INVALID) {
        return 0;
    } 

    //check box
    int counter = 0;
    for (int j = 0; j < (c.size); j++) {
        for (int k = box_corner; k < (box_corner + c.size); k++) {
            temp[counter++] = c.array[j * c.size * c.size + k];
        }
    }
    startp = temp;
    t = check_list(startp, c.size * c.size);
    if (t == INVALID) {
        return 0;
    } 

    return 1;
}
