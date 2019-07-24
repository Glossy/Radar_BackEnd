//package spring_with_netty.netty.util;
//
//
//import org.opencv.core.Core;
//import org.opencv.core.Mat;
//import org.opencv.core.CvType;
//
///**
// * The Kalman-Filter
// * @Author: Wu
// * @Date: 2019/5/27 8:50 PM
// */
//
//public class Filter {
//    Mat state_x;
//    Mat F;
//    Mat Q;
//    Mat R;
//    Mat P;
//    Mat H;
//    Mat y;
//    Mat S;
//    Mat K;
//
//
//    public Filter(int state_x_params, int measurement_params) { //6,4
//        //initialize F,Q,H.
//        this.state_x=new Mat(1,6,CvType.CV_32F);
//        this.F=new Mat(6,6,CvType.CV_32F);;
//        this.H=new Mat(4,6,CvType.CV_32F);
//        this.Q=new Mat(6,6,CvType.CV_32F);
//        this.y=new Mat(4,1,CvType.CV_32F);
//        this.R=new Mat(4,4,CvType.CV_32F);
//        this.S=new Mat(4,4,CvType.CV_32F);
//        this.K=new Mat(6,4,CvType.CV_32F);
//
//    }
//
//    public void predict() {
//
//        Core.gemm(this.state_x, this.F, 1, new Mat(), 0, this.state_x, 0);
//
//        Mat FT = new Mat();
//        Core.transpose(F, FT);
//        Mat FP=new Mat(6,6,CvType.CV_32F);
//        Core.gemm(this.F,this.P,1,new Mat(),0,FP,0);
//        Mat FPFT=new Mat(6,6,CvType.CV_32F);
//        Core.gemm(FP,FT,1,new Mat(),0,FPFT,0);
//        Core.add(FPFT, this.Q, this.P);
//    }
//
//    public void update(Mat z) {
//        Mat HT=new Mat();
//        Core.transpose(this.H, HT);
//        Mat state_xT=new Mat();
//        Core.transpose(this.state_x, state_xT);
//        Mat HXT=new Mat(4,1,CvType.CV_32F);
//
//        Core.gemm(this.H,state_xT,1,new Mat(),0,HXT,0);
//        Core.subtract(z, HXT, this.y);
//        Mat HP=new Mat(4,6,CvType.CV_32F);
//        Core.gemm(this.H,this.P,1,new Mat(),0,HP,0);
//        Mat HPHT=new Mat(4,4,CvType.CV_32F);
//        Core.gemm(HP,HT,1,new Mat(),0,HPHT,0);
//        Core.add(HPHT,this.R,this.S);
//        Mat S_inv=new Mat();
//        Core.invert(this.S, S_inv);
//        Mat PHT=new Mat(6,4,CvType.CV_32F);
//        Core.gemm(this.P,HT,1,new Mat(),0,PHT,0);
//        Core.gemm(PHT,S_inv,1,new Mat(),0,this.K,0);
//        Mat KY=new Mat(6,1,CvType.CV_32F);
//        Core.gemm(this.K,this.y,1,new Mat(),0,KY,0);
//        Mat KYT=new Mat();
//        Core.transpose(KY, KYT);
//        Core.add(this.state_x, KYT, this.state_x);
//
//        Mat KH=new Mat(6,6,CvType.CV_32F);
//        Core.gemm(this.K, this.H,1,new Mat(),0,KH,0);
//        Mat IKH=new Mat(6,6,CvType.CV_32F);
//        Core.subtract(Mat.eye(6,6, CvType.CV_32F), KH, IKH);
//        Core.gemm(IKH,this.P,1,new Mat(),0,this.P,0);
//
//    }
//
//}
