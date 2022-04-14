%%%%%%%%%%%%%%%%%%%%%%%% xy平面矢量振幅计算 %%%%%%%%%%%%%%%%%%%%%%%%
clear;
clc;
aa=xlsread('1.xlsx',2);
ax1=aa(:,1);
ay1=aa(:,2);
ax2=aa(:,4);
ay2=aa(:,5);
fs=25600;               % //默认为25600，可作为入参
N=length(ax1); %采样点数

%%%%%%%%%%%%%%%%%%%%%%%%字母说明%%%%%%%%%%%%%%%%%%%%%%%%
    %   单位：mm
    %   flcut：低频截止；fhcut：高频截止
    %   检测值：左侧矢量振幅A1（对应通道1、2），右侧矢量振幅A2（对应通道4、5） //该值为输出值，需要存库
%%%%%%%%%%%%%%%%%%%%%%%%%计算%%%%%%%%%%%%%%%%%%%%%%%%%% 
% //1、n为转频，作为入参；
% //2、滤波器：
% //1）每张图表有固定的滤波器设置；
% //2）也可以作为入参输入实现图表重新计算：flcut、fhcut作为入参；
n=0;             %设置转频为
if n==0
    flcut=0;fhcut=1;    %低频截止与高频截止
else
    flcut=n-0.25*n;    %低频截止
    fhcut=n+0.25*n;    %高频截止
end

[v1,x1]=a2v2x(ax1,fs,flcut,fhcut);
[v2,y1]=a2v2x(ay1,fs,flcut,fhcut);
[v3,x2]=a2v2x(ax2,fs,flcut,fhcut);
[v4,y2]=a2v2x(ay2,fs,flcut,fhcut);

% [leftx,lefty] = [x1*1000,y1*1000]  %*1000变换单位mm,左侧（对应通道1、2）XY数组 ////该值为输出值，需要存库
% [rightx,righty] = [x2*1000,y2*1000] %*1000变换单位mm,右侧（对应通道4、5）XY数组 ////该值为输出值，需要存库

%%%%%%%%%%%%%%%%用于绘图的数据%%%%%%%%%%%%%%%%%%
x_left=x1*1000;     %左侧水平方向位移（对应通道1）
y_left=y1*1000;     %左侧竖直平方向位移（对应通道2）
x_right=x2*1000;    %右侧水平方向位移（对应通道4）
y_right=y2*1000;    %右侧竖直方向位移（对应通道5）
%%%%%%%%%%%%%%%%绘图%%%%%%%%%%%%%%%%%%
figure;
plot(x_left,y_left);    %*1000变换单位mm
hold on;
plot(x_right,y_right);
xlabel('X方向振幅/mm');ylabel('y方向振幅/mm ');
title(['轴心轨迹图']);
ax = gca;
ax.XAxisLocation = 'origin';
ax.YAxisLocation = 'origin';
box off;

delta=1000*max([max(x1),max(y1),max(x2),max(y2)]);
x1=x1*1000+delta;
y1=y1*1000+delta;
x2=x2*1000+delta;
y2=y2*1000+delta;

xmax_1=max(x1);
ymmax_1=max(y1);
xmin_1=min(x1);
ymin_1=min(y1);
theta1=180*atan((ymmax_1-ymin_1)/(xmax_1-xmin_1))/pi;  %左侧（对应通道1、2）与水平方向夹角  //该值为输出值，需要存库

xmax_2=max(x2);
ymax_2=max(y2);
xmin_2=min(x2);
ymin_2=min(y2);
theta2=180*atan((ymax_2-ymin_2)/(xmax_2-xmin_2))/pi;   %右侧（对应通道4、5）与水平方向夹角  //该值为输出值，需要存库

dis_xy1=sqrt(x1.^2+y1.^2);
dis_xy2=sqrt(x2.^2+y2.^2);
[p1,m1]=max(dis_xy1);%正半轴
[p2,m2]=min(dis_xy1);%负半轴
[p3,m3]=max(dis_xy2);%正半轴
[p4,m4]=min(dis_xy2);%负半轴

A1=p1-p2;   %矢量幅值   //该值为输出值，需要存库
A2=p3-p4;   %矢量幅值   //该值为输出值，需要存库




