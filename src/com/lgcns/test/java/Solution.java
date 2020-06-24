package com.lgcns.test.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {
	
    public int[][] solution(int[][] arr1, int[][] arr2)
    {
        int[][] answer = new int[arr1.length][arr2[0].length];
        
        for (int i=0; i < arr1.length; i++) {
        	for (int j=0; j <arr2[0].length; j++) {
        		for (int k=0; k < arr2[0].length; k++) {
        			answer[i][j] += arr1[i][k] * arr2[k][j];
        		}
        	}
        }
        
        return answer;
    }

    public int[] solution1(String s)
    {
        int[] answer = {};
        
        StringTokenizer token = new StringTokenizer(s, "{");
        ArrayList<String> al = new ArrayList<>();
        
        while(token.hasMoreTokens()) {
        	al.add(removeBraces(token.nextToken()));
        }
        
        Collections.sort(al, new Sort());
        
        answer = new int[al.size()];
        
    	System.out.println("===========");
        for (int i=0; i < al.size(); i++) {
        	System.out.println(al.get(i));
        	
        	answer[i] = getElem(al.get(i), i);
        }
        
        return answer;
    }

    public int getElem(String s, int pos) {
        String[] arr = s.split(",");

    	return Integer.parseInt(arr[pos]); 
    }
    
    public String removeBraces(String s) {
    	StringBuilder sb = new StringBuilder();

    	for (int i=0; i < s.length(); i++) {
    		if (s.charAt(i) != '{' && s.charAt(i) != '}') {
        		sb.append(s.charAt(i));
    		}
    	}
    	return sb.toString(); 
    }
    
    class Sort implements Comparator<String>{
    	public int compare(String a, String b)
    	{
    		int cntA = countChar(a, ",");
    		int cntB = countChar(b, ",");
    		
    		return cntA - cntB;
    	}
    	
        public int countChar(String s, String d) {
            Pattern pattern = Pattern.compile(d);
            Matcher matcher = pattern.matcher(s);
            int count = 0;
            while (matcher.find()) {
                count++;
            }
            
            return count;
        }
    }    
   
    
	public static void main(String[] args) {
		Solution solver = new Solution();

		// 테스트용 입력값 설정
//		ArrayList<> params = new ArrayList<>();
//		params.add("Hello");
//		params.add("World");
		
		// 솔루션 실행
//		for (String param : params) {
//			int[][] a1 = {{1, 4}, {3, 2}, {4, 1}};
//			int[][] a2 = {{3, 3}, {3, 3}};
//			
			int[] result = solver.solution1("{{2},{2,1},{2,1,3},{2,1,3,4}}");
			
			for (int i=0; i < result.length; i++) {
				System.out.println(String.format("%d ", result[i]));
			}
//		}
	}

}
