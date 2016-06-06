package com.ash.util.video;

import com.ash.util.files.FileManager;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_videoio.*;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.OpenCVFrameConverter;

import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;

public class Smoother {
	
	public static void main(String[] args) {
		alternatingFastVideo(FileManager.getFileWithDialog("", true, null, null));
	}
	
    public static void smooth(String filename) { 
        IplImage image = cvLoadImage(filename);
        if (image != null) {
            cvSmooth(image, image);
            cvSaveImage(filename, image);
            cvReleaseImage(image);
        }
    }
    
    public static void multi(String filename){
    	IplImage img = cvLoadImage(filename);
		
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
    }
    
    @SuppressWarnings({ "unused", "null" })
	public static void alternatingFastVideo(String filename) {
    	FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(filename);
    	try {
			grabber.start();
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
    	
    	Frame temp = null;
    	IplImage sizef = null;
    	IplImage[] frame = new IplImage[2];
    	IplImage[] editedImage = new IplImage[2];
    	OpenCVFrameConverter.ToIplImage converterToIpl = new OpenCVFrameConverter.ToIplImage();
    	
    	cvNamedWindow("Video",CV_WINDOW_AUTOSIZE);
    	int frameCount = 0;
    	for(;;){
    		frameCount++;
    		long time = System.currentTimeMillis();
			try {
				
				temp = grabber.grab();
				if(temp==null){
					System.out.println("Error reading video or video finished, stopping");
					break;
				}
				
				try {
					frame[frameCount%2] = converterToIpl.convertToIplImage(temp);
				} catch(NullPointerException e){}
				
				System.out.println("FC" + frameCount + " Alt" + frameCount%2);
				//System.out.println(frame[frameCount%2] instanceof IplImage ? frame : "Not instanceof IplImage: " + frame);
				
				if(frame==null && temp!=null){
					System.err.println("Conversion error");
					cvShowImage("Video",frame[frameCount%2]);
				} else {
					sizef = frame[frameCount%2];
					System.out.println("frame[frameCount%2]==null:" + frame[frameCount%2]==null);
					editedImage[frameCount%2] = cvCreateImage(cvGetSize(frame[frameCount%2]),IPL_DEPTH_8U,1);
					cvCvtColor(frame[frameCount%2],editedImage[frameCount%2],CV_BGR2GRAY);
					cvShowImage("Video",editedImage[frameCount%2]);
				}
				//System.out.println(frame[0]==null||editedImage[0]==null||frame[1]==null||editedImage[1]==null);
				cvReleaseImage(frame[(frameCount-1)%2]);
				cvReleaseImage(editedImage[(frameCount-1)%2]);
			} catch (Exception e) {
				System.out.println("Error reading video...\n" + e.getMessage());
				break;
			}
			long deltaTime = System.currentTimeMillis() - time;
			System.out.println(deltaTime);
			char c = (char) cvWaitKey((int) Math.max((11 - deltaTime), 1));
			if(c==27) break;
		}
		try {
			grabber.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		cvDestroyWindow("Video");
		try {
			System.out.println("" + (frame[0]==null||editedImage[0]==null||frame[1]==null||editedImage[1]==null) + "\n\n\n\n");
			cvReleaseImage(frame[0]);
			cvReleaseImage(editedImage[0]);
			cvReleaseImage(frame[1]);
			cvReleaseImage(editedImage[1]);
		//cvReleaseImage((IplImage) null);
		} catch(NullPointerException e){}
    }
    
    public static void fastVideo(String filename) {
    	FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(filename);
    	try {
			grabber.start();
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
    	
    	Frame temp = null;
    	IplImage frame = null;
    	IplImage editedImage = null;
    	OpenCVFrameConverter.ToIplImage converterToIpl = new OpenCVFrameConverter.ToIplImage();
    	
    	cvNamedWindow("Video",CV_WINDOW_AUTOSIZE);
    	for(;;){
    		long time = System.currentTimeMillis();
			try {
				
				temp = grabber.grab();
				if(temp==null){
					System.out.println("Error reading video or video finished, stopping");
					break;
				}
				
				try {
					frame = converterToIpl.convertToIplImage(temp);
				} catch(NullPointerException e){}
				
				if(frame==null && temp!=null){
					System.err.println("Conversion error");
					cvShowImage("Video",frame);
					
					if(editedImage!=null) cvReleaseImage(editedImage);
				} else {
					editedImage = cvCreateImage(cvGetSize(frame),IPL_DEPTH_8U,1);
					cvCvtColor(frame,editedImage,CV_BGR2GRAY);
					cvShowImage("Video",editedImage);
				}
			} catch (Exception e) {
				System.out.println("Error reading video...\n" + e.getMessage());
				break;
			}
			long deltaTime = System.currentTimeMillis() - time;
			System.out.println(deltaTime);
			char c = (char) cvWaitKey((int) Math.max((11 - deltaTime), 1));
			if(c==27) break;
		}
		try {
			grabber.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		cvDestroyWindow("Video");
		try {
		cvReleaseImage(frame);
		cvReleaseImage(editedImage);
		cvReleaseImage((IplImage) null);
		} catch(NullPointerException e){}
    }
    
    public static void readVideo(String filename) {
    	//CvCapture capture = cvCreateFileCapture(filename);//FileManager.getFileWithDialog("Open video", true, null, ".mp4"));
    	//FFmpegFrameGrabber.
    	FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(filename);
    	try {
			grabber.start();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
    	
    	System.out.println("Reading vid.: " + filename + " Capture: " + grabber);
		IplImage frame;
		
		OpenCVFrameConverter.ToIplImage converterToIpl = new OpenCVFrameConverter.ToIplImage();
		cvNamedWindow("Video",CV_WINDOW_AUTOSIZE);
		
		for(;;){
			try {
				
				Frame f = grabber.grab();
				System.out.println("Frame pre-conversion: " + f);
				
				if(f==null){
					System.out.println("Error reading video or video finished, stopping");
					break;
				}
				
				frame = converterToIpl.convertToIplImage(f);
				System.out.println("Frame post-conversion: " + frame);
				
				if(frame==null && f!=null){
					System.err.println("Conversion error");
				}
				
			} catch (Exception e) {
				System.out.println("Error reading video...\n" + e.getMessage());
				break;
			}
			
			if(frame==null){
				System.out.println("? Error reading video...");
			}
			
			cvShowImage("Video",frame);
			char c = (char) cvWaitKey(30);
			
			if(c==27) break;
		}
		
		//cvReleaseCapture(capture);
		cvDestroyWindow("Video");
    }
    
}