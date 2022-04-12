%%%%%%%%%%%%%%%%%%%%%%%%位移（单轴加速度传感器使用）单值计算%%%%%%%%%%%%%%%%%%%%%%%%
clear;
clc;
c=8;
aa=xlsread('1417.xlsx',2);
a=aa(:,c);
fs=25600;
N=length(a); %采样点数
%%%%%%%%%%%%%%%%%%%%%%%%字母说明%%%%%%%%%%%%%%%%%%%%%%%%
    %   单位：gE
    %   flcut：低频截止；fhcut：高频截止
    %   检测值：峰峰值PPV（就是位移单值） //该值为输出值，需要存库
    %   p：峰值；m：峰值对应的频率
 %%%%%%%%%%%%%%%%%%%%%%%%计算%%%%%%%%%%%%%%%%%%%%%%%% 
% //1、转频为入参；
n=12;                  %设置转频为12
flcut=n-0.25*n;        %低频截止     
fhcut=n+0.25*n;        %高频截止
[v,x]=a2v2x(a,fs,flcut,fhcut);
x=x*1000;              %单位换算
[p1,m1]=max(x);        %正半轴
[p2,m2]=max(-x);       %负半轴
m1=m1/fs;
m2=m2/fs;
PPV=p1+p2;
%%%%%%%%%%%%%%%%%%%%%%%%用于输出图像的数据%%%%%%%%%%%%%%%%%%%%%%%%
t=(1:N)/fs; %横轴：时间
x_plot=x;   %纵轴：位移
%%%%%%%%%%%%%%%%%%%%%%%%图形示范%%%%%%%%%%%%%%%%%%%%%%%%   //图形示范部分不涉及，该部分为MatLab输出图形使用；
figure;
plot(t,x_plot);
xlabel('时间/s');ylabel('位移时域/ mm');
title(['通道',num2str(c),'的位移时域图','     位移单值：',num2str(PPV)]);
hold on;
plot([0,m1],[p1,p1],'r','linewidth',3);
plot([0,m2],[-p2,-p2],'r','linewidth',3);
s1=sprintf('(%2.2f, %2.2f)',m1,p1);
text(m1,p1,['峰值点：',s1]);
s2=sprintf('(%2.2f, %2.2f)',m2,-p2);
text(m2,-p2,['峰值点：',s2]);
