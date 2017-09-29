#include "header.h"

retVal_type check_list(int* list, int length)
{
    retVal_type t; 
    bool incomplete = false;
    int temp[length+1];
    //set all the values in the array to 0.
    memset(temp, 0x00,(length +1) * sizeof(int));
    int var;

    //Counts occurrences of each number  
    for (int i = 0; i < length;i++)
    {
        if (list[i]> length || list[i] < 0){
            t = INVALID;
            return t;
        }
        var = list[i];
        if (var != 0){
            if (temp[var] == 1){
                //if more than one occurrance of a number not 0, invalid list
                t = INVALID;
                return t;
            }
        } 
        temp[var] += 1;
        if (temp[0] > 0)
        {
            //if there are zeros in the list
            incomplete = true;
        }

    }

    if (incomplete) {
        t = INCOMPLETE;
    } else{
        t = COMPLETE;
    }
    return t;
}

retVal_type check_sudoku(sudoku c)
{
    retVal_type t;
    bool incomplete = false;
    int* startp;
    //New list for the boxes and columns. 
    int temp[c.size * c.size];
    
    //check rows
    for (int i =0; i < ((c.size *c.size));i++)
    {
        startp = &c.array[c.size *c.size * i];
        t = check_list(startp, c.size * c.size); 
        if (t == INVALID)
        {
            t = INVALID;
            return t;
        } 
        else{
            if (!incomplete && t == INCOMPLETE)
            {
                incomplete = true;
            }
        }
    }
    //check columns 
    for (int i =0; i < (c.size *c.size); i++){
        for (int j = 0; j < (c.size * c.size); j++){
            temp[j] = c.array[i + j *(c.size * c.size)];
        }
        startp = temp;
        t = check_list(startp, c.size * c.size); 
        if (t == INVALID)
        {
            t = INVALID;
            return t;
        } 
        else{
            if (!incomplete && t == INCOMPLETE)
            {
                incomplete = true;
            }
        }
    }
    //check boxes
    int var = 0;
   for (int i = 0; i < (c.size * c.size);i++)
    {
    int counter = 0;
        for (int j = 0; j < (c.size);j++)
        {
            for (int k=var; k < (var + c.size);k++)
            {
                temp[counter++] = c.array[j* c.size * c.size + k];
            }
        }
        //function to get the top left corner of the box for each iteration of the box.
        if ((i+1) % c.size != 0){
            var += c.size; 
        } else {
            var += c.size * c.size * c.size - (c.size -1)*c.size;
        }

        startp = temp;
        t = check_list(startp, c.size * c.size); 
        if (t == INVALID)
        {
            t = INVALID;
            return t;
        } 
        else{
            if (!incomplete && t == INCOMPLETE)
            {
                incomplete = true;
            }
        }
    }

    if (!incomplete)
    {
        t = COMPLETE;
    } else 
    {
        t = INCOMPLETE;
    }
    return t;
}