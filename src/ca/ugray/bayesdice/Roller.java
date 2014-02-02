package ca.ugray.bayesdice;

/*
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 */

public class Roller {
	private int penalty = 3;
	private int[] pastRolls = new int[11];
	private int lastRoll = -1;
	
	private double p1(int n){
		if (n<6){
			return (double) (n+1);
		}else{
			return (double) (11-n);
		}
	}
	
	private double doubCount(int n){
		return (double) pastRolls[n];
	}
	
	private double weight(int n){
		return p1(n)*Math.pow(2.0,-penalty*doubCount(n)/p1(n));
	}
	
	private double weightSum(){
		double sum=0.0;
		for(int i=0;i<pastRolls.length;i++){
			sum += weight(i);
		}
		return sum;
	}
	
	private double p(int n){
		return weight(n)/weightSum();
	}
	
	public int roll(){
		double cut = Math.random();
		double partialSum = p(0);
		int i = 0;
		while (partialSum < cut){
			i++;
			partialSum += p(i);
		}
		pastRolls[i]++;
		lastRoll=i;
		return i+2;
	}
	
	public int lastRoll(){
		return lastRoll+2;
	}
	
	public int count(int n){
		return pastRolls[n-2];
	}
	
	public int max(){
		int candidate=1; 
		for (int i=0;i<pastRolls.length;i++){
			candidate=Math.max(candidate, pastRolls[i]);
		}
		return candidate;
	}
	public Roller(){
	}
	public Roller(int p){
		penalty = p;
	}
	public void setPenalty(int p){
		penalty = p;
	}
	public int getPenalty(){
		return penalty;
	}

}
