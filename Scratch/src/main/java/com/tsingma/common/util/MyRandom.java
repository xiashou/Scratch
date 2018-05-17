package com.tsingma.common.util;

import java.util.Random;

public class MyRandom {

	/**
	 * Math.random()产生一个double型的随机数，判断一下 例如0出现的概率为%50，则介于0到0.50中间的返回0
	 * 
	 * @return int
	 * 
	 */
	public static int[] probabilityRandom(int[] ids, int[] probability, int length) throws Exception {
		Random r = new Random();
		int[] array = new int[length];
		int[] temp = new int[100];
		int j = 0;
		for (int i = 0; i < probability.length; i++) {
			int k = 0;
			while (k < probability[i] && j < 100) {
				temp[j] = ids[i];
				j++;
				k++;
			};
		}
		for (int i = 0; i < length; i++) {
			array[i] = temp[r.nextInt(100)];
		}
		return array;
	}

	public static int probabilityRandom(int[] ids, int[] probability) throws Exception {
		Random r = new Random();
		int[] temp = new int[100];
		int j = 0;
		for (int i = 0; i < probability.length; i++) {
			int k = 0;
			while (k < probability[i] && j < 100) {
				temp[j] = ids[i];
				j++;
				k++;
			};
		}
		return temp[r.nextInt(100)];
	}

	public static long generateRandomNumber(int n) {
		if (n < 1) {
			throw new IllegalArgumentException("随机数位数必须大于0");
		}
		return (long) (Math.random() * 9 * Math.pow(10, n - 1)) + (long) Math.pow(10, n - 1);
	}

	public static void main(String[] args) throws Exception {
//		System.out.println(generateRandomNumber(4));
		int ids[] = new int[]{1,2,3};
		int pro[] = new int[]{5,7,9};
		System.out.println(probabilityRandom(ids, pro));
	}

}
