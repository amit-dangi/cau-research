package com.sits.common;

import java.util.Arrays;
import java.util.Collections;

public class SecondLargest {

    public static void main(String[] args) {
        int a[] = {145, 155, 14, 141, 15, 117, 16};
    
        if (a.length < 2) {
            System.out.println("Array length should be at least 2 to find the second largest element.");
            return;
        }
        
        int firstLargest = a[0];
        int secondLargest = a[1]; // Initialize with second element
        
        if (firstLargest < secondLargest) {//115<116
            int temp = firstLargest;  // t=115
            firstLargest = secondLargest; //f=116
            secondLargest = temp; // s=115
        }
        
        for (int i = 2; i < a.length; i++) {
            if (a[i] > firstLargest) { // 16>117
                secondLargest = firstLargest;//s = 15
                firstLargest = a[i]; // f =117
            } else if (a[i] > secondLargest && a[i] != firstLargest) { //14> 115
                secondLargest = a[i];
            }
        }
        
        System.out.println("Second largest element is: " + secondLargest);
    }
}