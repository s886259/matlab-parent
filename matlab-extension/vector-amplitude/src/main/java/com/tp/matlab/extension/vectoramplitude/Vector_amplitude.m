%%%%%%%%%%%%%%%%%%%%%%%% xyƽ��ʸ��������� %%%%%%%%%%%%%%%%%%%%%%%%
clear;
clc;
aa=xlsread('1.xlsx',2);
ax1=aa(:,1);
ay1=aa(:,2);
ax2=aa(:,4);
ay2=aa(:,5);
fs=25600;               //Ĭ��Ϊ25600������Ϊ���
n=length(ax1); %��������

%%%%%%%%%%%%%%%%%%%%%%%%��ĸ˵��%%%%%%%%%%%%%%%%%%%%%%%%
    %   ��λ��mm
    %   flcut����Ƶ��ֹ��fhcut����Ƶ��ֹ
    %   ���ֵ�����ʸ�����A1����Ӧͨ��1��2�����Ҳ�ʸ�����A2����Ӧͨ��4��5�� //��ֵΪ���ֵ����Ҫ���
%%%%%%%%%%%%%%%%%%%%%%%%%����%%%%%%%%%%%%%%%%%%%%%%%%%% 
//1��nΪתƵ����Ϊ��Σ�
//2���˲�����
//1��ÿ��ͼ���й̶����˲������ã�
//2��Ҳ������Ϊ�������ʵ��ͼ�����¼��㣺flcut��fhcut��Ϊ��Σ�
n=12;             %����תƵΪ12
flcut=n-0.25*n;    %��Ƶ��ֹ
fhcut=n+0.25*n;    %��Ƶ��ֹ
[v1,x1]=a2v2x(ax1,fs,flcut,fhcut);
[v2,y1]=a2v2x(ay1,fs,flcut,fhcut);
[v3,x2]=a2v2x(ax2,fs,flcut,fhcut);
[v4,y2]=a2v2x(ay2,fs,flcut,fhcut);

[leftx,lefty] = [x1*1000,y1*1000]  %*1000�任��λmm,��ࣨ��Ӧͨ��1��2��XY���� ////��ֵΪ���ֵ����Ҫ���
[rightx,righty] = [x2*1000,y2*1000] %*1000�任��λmm,�Ҳࣨ��Ӧͨ��4��5��XY���� ////��ֵΪ���ֵ����Ҫ���

figure;
plot(x1*1000,y1*1000);    %*1000�任��λmm
hold on;
plot(x2*1000,y2*1000);
xlabel('X�������/mm');ylabel('y�������/mm ');
title(['���Ĺ켣ͼ']);
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
theta1=180*atan((ymmax_1-ymin_1)/(xmax_1-xmin_1))/pi;  %��ࣨ��Ӧͨ��1��2����ˮƽ����н�  //��ֵΪ���ֵ����Ҫ���

xmax_2=max(x2);
ymax_2=max(y2);
xmin_2=min(x2);
ymin_2=min(y2);
theta2=180*atan((ymax_2-ymin_2)/(xmax_2-xmin_2))/pi;   %�Ҳࣨ��Ӧͨ��4��5����ˮƽ����н�  //��ֵΪ���ֵ����Ҫ���

dis_xy1=sqrt(x1.^2+y1.^2);
dis_xy2=sqrt(x2.^2+y2.^2);
[p1,m1]=max(dis_xy1);%������
[p2,m2]=min(dis_xy1);%������
[p3,m3]=max(dis_xy2);%������
[p4,m4]=min(dis_xy2);%������

A1=p1-p2;   %ʸ����ֵ   //��ֵΪ���ֵ����Ҫ���
A2=p3-p4;   %ʸ����ֵ   //��ֵΪ���ֵ����Ҫ���
