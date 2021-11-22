package com.codeman.controller;

import java.util.Arrays;
import java.util.Random;

class Solution {
    int[] initial;
    int[] ans;
    static Random rm = new Random();

    public static void main(String[] args) {
        int a = rm.nextInt(3);

        System.out.println(a);
    }
    public Solution(int[] nums) {
        initial = Arrays.copyOf(nums, nums.length);
        this.ans = nums;
    }

    public int[] reset() {
        ans = Arrays.copyOf(initial, ans.length);
        return ans;
    }

    public int[] shuffle() {
        int a = rm.nextInt(ans.length);
        int b = rm.nextInt(ans.length);
        while (true) {
            if (b == a) b = rm.nextInt(ans.length);
            else break;
        }
        int temp = ans[a];
        ans[a] = ans[b];
        ans[a] = temp;
        return ans;
    }
}

/**
 * Your Solution object will be instantiated and called as such:
 * Solution obj = new Solution(nums);
 * int[] param_1 = obj.reset();
 * int[] param_2 = obj.shuffle();
 */
