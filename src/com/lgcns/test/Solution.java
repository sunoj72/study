package com.lgcns.test;

import java.util.ArrayList;

public class Solution {
	
    public int solution(String s)
    {
        int answer = 0;
        
        return answer;
    }

    
	public static void main(String[] args) {
		Solution solver = new Solution();

		// 테스트용 입력값 설정
		ArrayList<String> params = new ArrayList<>();
		params.add("Hello");
		params.add("World");
		
		// 솔루션 실행
		for (String param : params) {
			int result = solver.solution(param);
			System.out.println(String.format("Hello, Java: %d", result));
		}
	}

}
