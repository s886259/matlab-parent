%%%%%%%%%%%%%%%%%%%%%%%%包络时域图（检测：真峰峰值）%%%%%%%%%%%%%%%%%%%%%%%%
clear;
clc;
c=8;    %输入需要分析的通道序号
aa=xlsread('1417.xlsx',2);
a=aa(:,c);
g=9.80;
%%%%%%%%%%%%%%%%%%%%%%%%字母说明%%%%%%%%%%%%%%%%%%%%%%%%
    %   单位：gE
    %   time：总时间，时间范围：0~time    //该值为输出值，需要存库
    %   A：幅值   //该值为输出值，需要存库
    %   检测值：真峰峰值
    %   m：峰值出现的位置；
    %   p：峰值；tm：时域值（峰值对应的时间点）   //该值为输出值，需要存库
    %   rpp：真峰峰值  //该值为输出值，需要存库
    %   Pp：正峰值；Np：负峰值  //该值为输出值，需要存库
    %   vmean：均值 
    %   vrms：均方根值  //该值为输出值，需要存库
    %   sigma：标准偏差  //该值为输出值，需要存库
    %   pf：波峰因素  //该值为输出值，需要存库
    %   ske：偏斜度  //该值为输出值，需要存库
    %   kur：峭度  //该值为输出值，需要存库
    %   TV：振动总值，gE，用于计算整体趋势  //该值为输出值，需要存库
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%    
% //1、采样频率和数据长度为入参；
% //2、滤波器：
% //1）每张图表有固定的滤波器设置；
% //2）也可以作为入参输入实现图表重新计算：fmin、fmax、flcut、fhcut等字段；
% //3、使用的是汉宁窗函数；
fs=25600;        %采样频率
N=length(a);     %数据长度
fmin=0;          %fmin：起始频率
fmax=1000;       %famx：终止频率
flcut=500;       %低频截止
fhcut=fs/2.56;   %高频截止
df=fs/N;
time=N/fs;
[a_fir]=hann_filt(a,fs,flcut,fhcut);    %500~10k的过滤器范围
[p,m]=findpeaks(a_fir);
p=p/g;
m=m/fs;
[pv,tm]=max(p);
tm=m(tm);
A=pv;
[Pp,Np]=Value_of_Peak(p);
rpp=abs(Pp)+abs(Np);
[vmean]=Mean_Value(p);
[sigma]=Value_of_Sigma(p,vmean);
[vrms]=Value_of_RMS(p);
pf=pv/vrms;
[ske]=Value_of_Skewness(p,vmean);
[kur]=Value_of_Kurtosis(p,vmean,sigma);
[TV]=total_value(a,fs,fmin,fmax);
TV=TV/g;  %单位转换
%%%%%%%%%%%%%%%%%%%%%%%%图形示范%%%%%%%%%%%%%%%%%%%%%%%%   //图形示范部分不涉及，该部分为MatLab输出图形使用；
figure;
plot(m,p);
xlabel('时间/s');ylabel('加速度包络时域/ gE');
title(['通道',num2str(c),'的加速度包络时域图']);
hold on;
plot([0,tm],[pv,pv],'r','linewidth',3);
s1=sprintf('(%2.2f, %2.2f)',tm,pv);
text(tm,pv,['峰值点：',s1]);
hold on;
text(0.2,pv,['真峰峰值：',num2str(rpp)]);
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

