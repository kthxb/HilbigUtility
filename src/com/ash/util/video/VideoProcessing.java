package com.ash.util.video;

//import static org.bytedeco.javacpp.opencv_core.*;
//import static org.bytedeco.javacpp.opencv_highgui.*;

import com.googlecode.javacv.*;
import com.googlecode.javacv.cpp.*;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui.CvCapture;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_calib3d.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;

import com.ash.util.files.FileManager;

import static com.googlecode.javacv.cpp.opencv_highgui.*;

@SuppressWarnings("unused")
public class VideoProcessing {
	
	public static void main(String[] args) {
		
		IplImage img = cvLoadImage(FileManager.getFileWithDialog("Open img", true, null, ".jpg"));
		
		IplImage hsvimg = cvCreateImage(cvGetSize(img),IPL_DEPTH_8U,3);
		IplImage grayimg = cvCreateImage(cvGetSize(img),IPL_DEPTH_8U,1);
		
		cvCvtColor(img,hsvimg,CV_BGR2HSV);
		cvCvtColor(img,grayimg,CV_BGR2GRAY);
		
		cvShowImage("Original",img);
		cvShowImage("HSV",hsvimg);
		cvShowImage("GRAY",grayimg);
		cvWaitKey();
		
		cvSaveImage("Original.jpg",img);
		cvSaveImage("HSV.jpg",hsvimg);
		cvSaveImage("GRAY.jpg",grayimg);
		
		cvReleaseImage(img);
		cvReleaseImage(hsvimg);
		cvReleaseImage(grayimg);
		
		
		
		/*
		CvCapture capture = cvCreateFileCapture("D:/Videos/1v1_2_hb.mp4");//FileManager.getFileWithDialog("Open video", true, null, ".mp4"));
		IplImage frame;
		cvNamedWindow("Video",CV_WINDOW_AUTOSIZE);
		
		for(;;){
			frame = cvQueryFrame(capture);
			
			cvShowImage("Video",frame);
			char c = (char) cvWaitKey(30);
			if(c==27) break;
		}
		
		cvReleaseCapture(capture);
		cvDestroyWindow("Video");*/
	}
	
}
