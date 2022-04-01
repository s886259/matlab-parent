%%%%%%%%%%%%%%%%%%%%%%%%加速度时域图（检测：峰值）%%%%%%%%%%%%%%%%%%%%%%%%
clear;
clc;
c=8;    %输入需要分析的通道序号
aa=xlsread('1417.xlsx',2);
a=aa(:,c);
%%%%%%%%%%%%%%%%%%%%%%%%字母说明%%%%%%%%%%%%%%%%%%%%%%%%
    %   单位：m/s^2
    %   time：总时间，时间范围：0~time //该值为输出值，需要存库
    %   RPM：实际转速
    %   A：幅值 //该值为输出值，需要存库
    %   检测值：峰值
    %   m：峰值出现的位置； 
    %   p：峰值；tm：时域值（峰值对应的时间点）//该值为输出值，需要存库
    %   Pp：正峰值；Np：负峰值 //该值为输出值，需要存库
    %   vmean：均值 
    %   vrms：均方根值 //该值为输出值，需要存库
    %   sigma：标准偏差 //该值为输出值，需要存库
    %   pf：波峰因素  //该值为输出值，需要存库
    %   ske：偏斜度  //该值为输出值，需要存库
    %   kur：峭度  //该值为输出值，需要存库
    %   TV：振动总值，m/s^2（用于计算整体趋势）  //该值为输出值，需要存库
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%    
% //1、采样频率和数据长度为入参；
% //2、滤波器：
% //1）每张图表有固定的滤波器设置；
% //2）也可以作为入参输入实现图表重新计算：fmin、fmax、flcut、fhcut等字段；
% //3、使用的是汉宁窗函数；
fs=25600;        %采样频率
N=length(a);     %数据长度
fmin=0;          %fmin：起始频率
fmax=10000;      %famx：终止频率
flcut=5;         %低频截止
fhcut=fs/2.56;   %高频截止
df=fs/N;
time=N/fs;       %总时间
[a_fir]=hann_filt(fs,a,flcut,fhcut);
[p,m]=max(a_fir);
tm=m/fs;
A=p;
[Pp,Np]=Value_of_Peak(a_fir);
[vmean]=Mean_Value(a_fir);
[sigma]=Value_of_Sigma(a_fir,vmean);
[vrms]=Value_of_RMS(a_fir);
pf=p/vrms;
[ske]=Value_of_Skewness(a_fir,vmean);
[kur]=Value_of_Kurtosis(a_fir,vmean,sigma);
[TV]=total_value(a_fir,fs,fmin,fmax);
%%%%%%%%%%%%%%%%%%%%%%%%图形示范%%%%%%%%%%%%%%%%%%%%%%%%   //图形示范部分不涉及，该部分为MatLab输出图形使用；
t=(0:N-1)/fs;
figure;
plot(t,a_fir);
xlabel('时间/s');ylabel('加速度时域/ (m/s^2)');
title(['通道',num2str(c),'的加速度时域图']);
hold on;
plot([0,tm],[p,p],'r','linewidth',3);
s1=sprintf('(%2.2f, %2.2f)',tm,p);
text(tm,p,['峰值点：',s1]);
%%%%%%%%%%%%%%%%%%%%%%%%其他解释%%%%%%%%%%%%%%%%%%%%%%%%
%   sk=skewness(a_fir);%matlab内部的偏度函数（可用于对比我的程序的准确度）
%   ku=kurtosis(a_fir);%matlab内部的峭度函数（可用于对比我的程序的准确度）
%   length（a）；计算数组a的长度
%   findpeaks(a)；寻找数组a的最大值，得到最值p和其对应的序号m
%   max(a)；数组a的最大值
%   min(a)；数组a的最小值
%   fft；快速傅里叶变换
%   ifft；傅里叶逆变换
%   detrend；去除趋势项

