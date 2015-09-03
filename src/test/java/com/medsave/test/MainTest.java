package com.medsave.test;

import java.io.IOException;

import com.medcloud.core.entities.Image;

public class MainTest {

	public static void main(String[] args) throws IllegalStateException, IOException {
		
		Image image = new Image();
		image.setCid("CID01");
		image.setIid("IID01");
		image.setPid("PID01");
		image.setSno("SNO01");
		image.setSid("SID01");
		
//		System.out.println(TreeUtils.getTreeByImage(image));
		
		
	}
	
}
