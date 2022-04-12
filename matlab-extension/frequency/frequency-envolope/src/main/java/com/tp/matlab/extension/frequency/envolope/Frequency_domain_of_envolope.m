%%%%%%%%%%%%%%%%%%%%%%%%包络频谱图（轴承）%%%%%%%%%%%%%%%%%%%%%%%%
clear;
clc;
c=8;    %输入需要分析的通道序号
aa=xlsread('1417.xlsx',2);
a=aa(:,c);
g=9.8;
%%%%%%%%%%%%%%%%%%%%%%%%字母说明%%%%%%%%%%%%%%%%%%%%%%%%
    %   单位：gE
    %   fcut：低频截止
    %   fmin：开始频率；fmax：结束频率；fmin~fmax为频率范围
    %   fbegin~fstop：过滤器范围，500~10k
    %   ymax：满刻度
    %   检测值：峰值
    %   p：峰值；mf：峰值对应的频率
    %   TV：振动总值=整体频谱=整体趋势，m/s^2    //该值为输出值，需要存库
    %   光标信息：见下面光标信息后的注释
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%    
% //1、采样频率和数据长度为入参；
% //2、滤波器：
% //1）每张图表有固定的滤波器设置；
% //2）也可以作为入参输入实现图表重新计算：fmin、fmax、flcut、fhcut等字段；
fs=25600;          %采样频率
N=length(a);       %数据长度
fmin=0;            %fmin：起始频率
fmax=1000;         %famx：终止频率
flcut=500;         %低频截止
fhcut=10000;       %高频截止
df=fs/N;
[a_fir]=hann_filt(a,fs,flcut,fhcut);
a_fir_2=a_fir.^2;
[a_fir_3]=hann_filt(a_fir_2,fs,flcut,fhcut);
[f,ai]=spectrum(fs,a_fir_3);    %ai用于存储频谱幅值数据
[p,m]=max(ai(1:fmax));  %寻峰
mf=f(m);    %峰值对应频率值
[TV]=total_value(a_fir,fs,fmin,fmax);  %整体频谱 (也是 整体趋势）
%%%%%%%%%%%%%%%%%%%%%%%FAM栏计算%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  
% //1、BPFI、BPFO、BSF、FTF、n为输入变量，入参；
% //2、默认K1的倍频为1、2、3、4去乘以计算，但也可以作为输入变量，作为入参；
BPFI=9.429032;BPFO=6.570968;BSF=2.645376;FTF=0.410686;
n=12;                      %转频(输入变量）
k1=n*[1 2 3 4];            %倍频
f_BPFI=k1*BPFI;f_BPFO=k1*BPFO;f_BSF=k1*BSF;f_FTF=k1*FTF;
num_BPFI=floor(f_BPFI/df)+1;
num_BPFO=floor(f_BPFO/df)+1;
num_BSF=floor(f_BSF/df)+1;
num_FTF=floor(f_FTF/df)+1;
r_BPFI=f(num_BPFI);      %BPFI实际频率
r_BPFO=f(num_BPFO);      %PBF0实际频率
r_BSF=f(num_BSF);       %BSF实际频率
r_FTF=f(num_FTF);       %FIF实际频率
valu_BPFI=ai(num_BPFI);  %BPFI幅值
valu_BPFO=ai(num_BPFO);  %BPF0幅值
valu_BSF=ai(num_BSF);    %BSF幅值
valu_FTF=ai(num_FTF);    %BFIF幅值
output_BPFI=[r_BPFI',valu_BPFI];   %输出BPFI的频率和幅值   
output_BPFO=[r_BPFO',valu_BPFO];   %输出BPFO的频率和幅值  
output_BSF=[r_BSF',valu_BSF];      %输出BSF的频率和幅值    
output_FTF=[r_FTF',valu_FTF];      %输出FTF的频率和幅值    

BPFI_1=output_BPFI(1,:);          %输出BPFI*1的频率和幅值 //该值为输出值，需要存库，bpfi1
BPFI_2=output_BPFI(2,:);          %输出BPFI*2的频率和幅值 //该值为输出值，需要存库，bpfi2
BPFI_3=output_BPFI(3,:);          %输出BPFI*3的频率和幅值 //该值为输出值，需要存库，bpfi3
BPFI_4=output_BPFI(4,:);          %输出BPFI*4的频率和幅值 //该值为输出值，需要存库，bpfi4

BPFO_1=output_BPFO(1,:);          %输出BPFO*1的频率和幅值 //该值为输出值，需要存库，bpfo1
BPFO_2=output_BPFO(2,:);          %输出BPFO*2的频率和幅值 //该值为输出值，需要存库，bpfo2
BPFO_3=output_BPFO(3,:);          %输出BPFO*3的频率和幅值 //该值为输出值，需要存库，bpfo3
BPFO_4=output_BPFO(4,:);          %输出BPFO*4的频率和幅值 //该值为输出值，需要存库，bpfo4

BSF_1=output_BSF(1,:);            %输出BSF*1的频率和幅值 //该值为输出值，需要存库，bsf1
BSF_2=output_BSF(2,:);            %输出BSF*2的频率和幅值 //该值为输出值，需要存库，bsf2
BSF_3=output_BSF(3,:);            %输出BSF*3的频率和幅值 //该值为输出值，需要存库，bsf3
BSF_4=output_BSF(4,:);            %输出BSF*4的频率和幅值 //该值为输出值，需要存库，bsf4

FTF_1=output_FTF(1,:);            %输出FTF*1的频率和幅值 //该值为输出值，需要存库，ftf1
FTF_2=output_FTF(2,:);            %输出FTF*2的频率和幅值 //该值为输出值，需要存库，ftf2
FTF_3=output_FTF(3,:);            %输出FTF*3的频率和幅值 //该值为输出值，需要存库，ftf3
FTF_4=output_FTF(4,:);            %输出FTF*4的频率和幅值 //该值为输出值，需要存库，ftf3

%%%%%%%%%%%%%%%%%%%%%%%谐波光标计算%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 
% //1、n为输入变量，入参； 
% //2、默认K2谐波为1、2、3、4、5、6、7、8、9、10去乘以计算，但也可以作为输入变量，作为入参；
n=12;                           %转频
num_n=floor(n/df)+1;
k2=[1 2 3 4 5 6 7 8 9 10];      %谐波
f_xiebo=k2*n;   %谐波
num_f=floor(f_xiebo/df)+1;
[valu_xiebo]=f(num_f);          %谐波频率
[fuzhi_xiebo]=ai(num_f);        %谐波幅值
percent=100*(fuzhi_xiebo./fuzhi_xiebo(1)); %相对于基频的百分比
xiebo=[valu_xiebo',fuzhi_xiebo,percent];  %输出【频率 幅值 相对百分比】 //该值为输出值，需要存库

xiebo_1=xiebo(1,:);                %输出谐波为1时【频率 幅值 相对百分比】//对应K2的第1个值，该值为输出值，需要存库 harmonic1
xiebo_2=xiebo(2,:);                %输出谐波为2时【频率 幅值 相对百分比】//对应K2的第2个值，该值为输出值，需要存库 harmonic2
xiebo_3=xiebo(3,:);                %输出谐波为3时【频率 幅值 相对百分比】//对应K2的第3个值，该值为输出值，需要存库 harmonic3
xiebo_4=xiebo(4,:);                %输出谐波为4时【频率 幅值 相对百分比】//对应K2的第4个值，该值为输出值，需要存库 harmonic4
xiebo_5=xiebo(5,:);                %输出谐波为5时【频率 幅值 相对百分比】//对应K2的第5个值，该值为输出值，需要存库 harmonic5
xiebo_6=xiebo(6,:);                %输出谐波为6时【频率 幅值 相对百分比】//对应K2的第6个值，该值为输出值，需要存库 harmonic6
xiebo_7=xiebo(7,:);                %输出谐波为7时【频率 幅值 相对百分比】//对应K2的第7个值，该值为输出值，需要存库 harmonic7
xiebo_8=xiebo(8,:);                %输出谐波为8时【频率 幅值 相对百分比】//对应K2的第8个值，该值为输出值，需要存库 harmonic8
xiebo_9=xiebo(9,:);                %输出谐波为9时【频率 幅值 相对百分比】//对应K2的第9个值，该值为输出值，需要存库 harmonic9
xiebo_10=xiebo(10,:);              %输出谐波为10时【频率 幅值 相对百分比】//对应K2的第10个值，该值为输出值，需要存库 harmonic10

%%%%%%%%%%%%%%%%%%%%%%%边带计算%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  
% //1、n为输入变量，入参； 
% //2、默认position为-5、-4、-3、-2 -1 0 1 2 3 4 5，但也可以作为输入变量，作为入参；
position=[-5 -4 -3 -2 -1 0 1 2 3 4 5];     %位置
f1=fmax/2+n*position;
num_f1=floor(f1/df)+1;
num_zx=floor((fmax/2)/df)+1;
[fuzhi_biandai]=ai(num_f1);                %幅值
[valu_biandai]=f(num_f1);                  %频率
k=[valu_biandai]./(n+eps);                       %阶次
dB=20*log10([fuzhi_biandai]./ai(num_zx));  %dB
biandai=[position',valu_biandai',fuzhi_biandai,k',dB] ;%输出【位置 频率 幅值 阶次 dB】

biandai_1=biandai(1,:);                    %输出不同位置的【位置 频率 幅值 阶次 dB】//，对应position的第1个值，该值为输出值，需要存库 sidcband1
biandai_2=biandai(2,:);                    %输出不同位置的【位置 频率 幅值 阶次 dB】//，对应position的第2个值，该值为输出值，需要存库 sidcband2
biandai_3=biandai(3,:);                    %输出不同位置的【位置 频率 幅值 阶次 dB】//，对应position的第3个值，该值为输出值，需要存库 sidcband3
biandai_4=biandai(4,:);                    %输出不同位置的【位置 频率 幅值 阶次 dB】//，对应position的第4个值，该值为输出值，需要存库 sidcband4
biandai_5=biandai(5,:);                    %输出不同位置的【位置 频率 幅值 阶次 dB】//，对应position的第5个值，该值为输出值，需要存库 sidcband5
biandai_6=biandai(6,:);                    %输出不同位置的【位置 频率 幅值 阶次 dB】//，对应position的第6个值，该值为输出值，需要存库 sidcband6
biandai_7=biandai(7,:);                    %输出不同位置的【位置 频率 幅值 阶次 dB】//，对应position的第7个值，该值为输出值，需要存库 sidcband7
biandai_8=biandai(8,:);                    %输出不同位置的【位置 频率 幅值 阶次 dB】//，对应position的第8个值，该值为输出值，需要存库 sidcband8
biandai_9=biandai(9,:);                    %输出不同位置的【位置 频率 幅值 阶次 dB】//，对应position的第9个值，该值为输出值，需要存库 sidcband9
biandai_10=biandai(10,:);                  %输出不同位置的【位置 频率 幅值 阶次 dB】//，对应position的第10个值，该值为输出值，需要存库 sidcband10
biandai_11=biandai(11,:);                  %输出不同位置的【位置 频率 幅值 阶次 dB】//，对应position的第11个值，该值为输出值，需要存库 sidcband11
%%%%%%%%%%%%%%%%%%%%%%%%用于作图的数据%%%%%%%%%%%%%%%%%%%%%%%%
f_plot=f;   %横轴：频率
Am_plot=ai; %纵轴：幅值
%%%%%%%%%%%%%%%%%%%%%%%%图形示例%%%%%%%%%%%%%%%%%%%%%%%%   //图形示范部分不涉及，该部分为MatLab输出图形使用；
figure;
plot(f_plot,Am_plot);
xlim([fmin,fmax]); 
ylim([0,1.5*p]); 
title(['通道',num2str(c),'的包络频谱图']);
xlabel('频率      Hz');
ylabel('幅值      gE');
hold on;
%plot([0,mf],[p,p],'r','linewidth',3);
%s1=sprintf('(%2.5f, %2.5f)',mf,p);
%text('rotation',90 );
%text(mf,p,['峰值点：',s1]);
%text(mf,p,['峰值点：',s1],'rotation',90);
%标注文字
for i=1:4
    p1=num_FTF(i);
    p2=valu_FTF(i);
    p3=num_BSF(i);
    p4=valu_BSF(i);
    p5=num_BPFO(i);
    p6=valu_BPFO(i);
    p7=num_BPFI(i);
    p8=valu_BPFI(i);
    text(p1,1.2*p,['FTF',num2str(i)],'rotation',90);
    text(p3,1.2*p,['BSF',num2str(i)],'rotation',90);
    text(p5,1.2*p,['BPFO',num2str(i)],'rotation',90);
    text(p7,1.2*p,['BPFI',num2str(i)],'rotation',90);
    plot([p1,p1],[0,1.2*p],'-- r');
    plot([p3,p3],[0,1.2*p],'-- r');
    plot([p5,p5],[0,1.2*p],'-- r');
    plot([p7,p7],[0,1.2*p],'-- r');
end



