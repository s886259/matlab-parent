%%%%%%%%%%%%%%%%%%%%%%%%�ٶ�Ƶ��ͼ����������%%%%%%%%%%%%%%%%%%%%%%%%
clear;
clc;
c=8;    %������Ҫ������ͨ�����
aa=xlsread('1.xlsx',2);
a=aa(:,c);
%%%%%%%%%%%%%%%%%%%%%%%%��ĸ˵��%%%%%%%%%%%%%%%%%%%%%%%%
    %   ��λ��mm/s
    %   fcut����Ƶ��ֹ
    %   fmin����ʼƵ�ʣ�fmax������Ƶ�ʣ�fmin~fmaxΪƵ�ʷ�Χ
    %   ���ֵ��������ֵ
    %   rms��������ֵ
    %   p����ֵ��mf����ֵ��Ӧ��Ƶ��
    %   TV������ֵ=����Ƶ��=�������ƣ�m/s^2    //��ֵΪ���ֵ����Ҫ���
    %   �����Ϣ������������Ϣ���ע��
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%    
% //1������Ƶ�ʺ����ݳ���Ϊ��Σ�
% //2���˲�����
% //1��ÿ��ͼ���й̶����˲������ã�
% //2��Ҳ������Ϊ�������ʵ��ͼ�����¼��㣺fmin��fmax��flcut��fhcut���ֶΣ�
n=0;           %����תƵ
fs=25600;      %����Ƶ��
N=length(a);   %���ݳ���
fmin=0;        %fmin����ʼƵ��
fmax=500;      %famx����ֹƵ��
flcut=5;       %��Ƶ��ֹ
fhcut=fs/2.56; %��Ƶ��ֹ
df=fs/N;
[v]=a2v(a,fs,flcut,fhcut);
v=v';
[v]=hann_filt(v,fs,flcut,fhcut);
[f,vi]=spectrum(fs,v);    %ai���ڴ洢Ƶ�׷�ֵ����
[p,m]=max(vi);  %Ѱ��
mf=f(m);    %��ֵ��ӦƵ��ֵ
[vrms]=Value_of_RMS(v);
[TV]=total_value(v,fs,fmin,fmax);  %����Ƶ�� (Ҳ���������ƣ�    //��ֵΪ���ֵ����Ҫ���
if n==0
    fuzhi_zhuanpin=0;                         % תƵΪ0ʱ�ķ�ֵ
else
    num_n=floor(n/df)+1;      
    fuzhi_zhuanpin=vi(num_n);                  %תƵ��Ϊ0��Ӧ�ķ�ֵ
end
%%%%%%%%%%%%%%%%%%%%%%%FAM������%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  
% //1��BPFI��BPFO��BSF��FTF��nΪ�����������Σ�
% //2��Ĭ��K1�ı�ƵΪ1��2��3��4ȥ���Լ��㣬��Ҳ������Ϊ�����������Ϊ��Σ�
BPFI=9.429032;BPFO=6.570968;BSF=2.645376;FTF=0.410686;
k1=n*[1 2 3 4];            %г��
f_BPFI=k1*BPFI;f_BPFO=k1*BPFO;f_BSF=k1*BSF;f_FTF=k1*FTF;
num_BPFI=floor(f_BPFI/df)+1;
num_BPFO=floor(f_BPFO/df)+1;
num_BSF=floor(f_BSF/df)+1;
num_FTF=floor(f_FTF/df)+1;
r_BPFI=f(num_BPFI);      %BPFIʵ��Ƶ��
r_BPFO=f(num_BPFO);      %PBF0ʵ��Ƶ��
r_BSF=f(num_BSF);       %BSFʵ��Ƶ��
r_FTF=f(num_FTF);       %FIFʵ��Ƶ��
valu_BPFI=vi(num_BPFI);  %BPFI��ֵ
valu_BPFO=vi(num_BPFO);  %BPF0��ֵ
valu_BSF=vi(num_BSF);    %BSF��ֵ
valu_FTF=vi(num_FTF);    %BFIF��ֵ
output_BPFI=[r_BPFI',valu_BPFI'];   %���BPFI��Ƶ�ʺͷ�ֵ
output_BPFO=[r_BPFO',valu_BPFO'];   %���BPFO��Ƶ�ʺͷ�ֵ
output_BSF=[r_BSF',valu_BSF'];      %���BSF��Ƶ�ʺͷ�ֵ
output_FTF=[r_FTF',valu_FTF'];      %���FTF��Ƶ�ʺͷ�ֵ

BPFI_1=output_BPFI(1,:);          %���BPFI*1��Ƶ�ʺͷ�ֵ //��ֵΪ���ֵ����Ҫ��⣬bpfi1
BPFI_2=output_BPFI(2,:);          %���BPFI*2��Ƶ�ʺͷ�ֵ //��ֵΪ���ֵ����Ҫ��⣬bpfi2
BPFI_3=output_BPFI(3,:);          %���BPFI*3��Ƶ�ʺͷ�ֵ //��ֵΪ���ֵ����Ҫ��⣬bpfi3
BPFI_4=output_BPFI(4,:);          %���BPFI*4��Ƶ�ʺͷ�ֵ //��ֵΪ���ֵ����Ҫ��⣬bpfi4

BPFO_1=output_BPFO(1,:);          %���BPFO*1��Ƶ�ʺͷ�ֵ //��ֵΪ���ֵ����Ҫ��⣬bpfo1
BPFO_2=output_BPFO(2,:);          %���BPFO*2��Ƶ�ʺͷ�ֵ //��ֵΪ���ֵ����Ҫ��⣬bpfo2
BPFO_3=output_BPFO(3,:);          %���BPFO*3��Ƶ�ʺͷ�ֵ //��ֵΪ���ֵ����Ҫ��⣬bpfo3
BPFO_4=output_BPFO(4,:);          %���BPFO*4��Ƶ�ʺͷ�ֵ //��ֵΪ���ֵ����Ҫ��⣬bpfo4

BSF_1=output_BSF(1,:);            %���BSF*1��Ƶ�ʺͷ�ֵ //��ֵΪ���ֵ����Ҫ��⣬bsf1
BSF_2=output_BSF(2,:);            %���BSF*2��Ƶ�ʺͷ�ֵ //��ֵΪ���ֵ����Ҫ��⣬bsf2
BSF_3=output_BSF(3,:);            %���BSF*3��Ƶ�ʺͷ�ֵ //��ֵΪ���ֵ����Ҫ��⣬bsf3
BSF_4=output_BSF(4,:);            %���BSF*4��Ƶ�ʺͷ�ֵ //��ֵΪ���ֵ����Ҫ��⣬bsf4

FTF_1=output_FTF(1,:);            %���FTF*1��Ƶ�ʺͷ�ֵ //��ֵΪ���ֵ����Ҫ��⣬ftf1
FTF_2=output_FTF(2,:);            %���FTF*2��Ƶ�ʺͷ�ֵ //��ֵΪ���ֵ����Ҫ��⣬ftf2
FTF_3=output_FTF(3,:);            %���FTF*3��Ƶ�ʺͷ�ֵ //��ֵΪ���ֵ����Ҫ��⣬ftf3
FTF_4=output_FTF(4,:);            %���FTF*4��Ƶ�ʺͷ�ֵ //��ֵΪ���ֵ����Ҫ��⣬ftf3

%%%%%%%%%%%%%%%%%%%%%%%г��������%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 
% //1��nΪ�����������Σ� 
% //2��Ĭ��K2г��Ϊ1��2��3��4��5��6��7��8��9��10ȥ���Լ��㣬��Ҳ������Ϊ�����������Ϊ��Σ� 
k2=[1 2 3 4 5 6 7 8 9 10];   %г��
f_xiebo=k2*n;   %г��
num_f=floor(f_xiebo/df)+1;
[valu_xiebo]=f(num_f);        %г��Ƶ��
[fuzhi_xiebo]=vi(num_f);        %г����ֵ
if n==0
    percent=zeros(1,10);
else
    percent=100*(fuzhi_xiebo./fuzhi_xiebo(1)); %����ڻ�Ƶ�İٷֱ�
end
xiebo=[valu_xiebo',fuzhi_xiebo',percent'];  %�����Ƶ�� ��ֵ ��԰ٷֱȡ�


xiebo_1=xiebo(1,:);                %���г��Ϊ1ʱ��Ƶ�� ��ֵ ��԰ٷֱȡ�//��ӦK2�ĵ�1��ֵ����ֵΪ���ֵ����Ҫ��� harmonic1
xiebo_2=xiebo(2,:);                %���г��Ϊ2ʱ��Ƶ�� ��ֵ ��԰ٷֱȡ�//��ӦK2�ĵ�2��ֵ����ֵΪ���ֵ����Ҫ��� harmonic2
xiebo_3=xiebo(3,:);                %���г��Ϊ3ʱ��Ƶ�� ��ֵ ��԰ٷֱȡ�//��ӦK2�ĵ�3��ֵ����ֵΪ���ֵ����Ҫ��� harmonic3
xiebo_4=xiebo(4,:);                %���г��Ϊ4ʱ��Ƶ�� ��ֵ ��԰ٷֱȡ�//��ӦK2�ĵ�4��ֵ����ֵΪ���ֵ����Ҫ��� harmonic4
xiebo_5=xiebo(5,:);                %���г��Ϊ5ʱ��Ƶ�� ��ֵ ��԰ٷֱȡ�//��ӦK2�ĵ�5��ֵ����ֵΪ���ֵ����Ҫ��� harmonic5
xiebo_6=xiebo(6,:);                %���г��Ϊ6ʱ��Ƶ�� ��ֵ ��԰ٷֱȡ�//��ӦK2�ĵ�6��ֵ����ֵΪ���ֵ����Ҫ��� harmonic6
xiebo_7=xiebo(7,:);                %���г��Ϊ7ʱ��Ƶ�� ��ֵ ��԰ٷֱȡ�//��ӦK2�ĵ�7��ֵ����ֵΪ���ֵ����Ҫ��� harmonic7
xiebo_8=xiebo(8,:);                %���г��Ϊ8ʱ��Ƶ�� ��ֵ ��԰ٷֱȡ�//��ӦK2�ĵ�8��ֵ����ֵΪ���ֵ����Ҫ��� harmonic8
xiebo_9=xiebo(9,:);                %���г��Ϊ9ʱ��Ƶ�� ��ֵ ��԰ٷֱȡ�//��ӦK2�ĵ�9��ֵ����ֵΪ���ֵ����Ҫ��� harmonic9
xiebo_10=xiebo(10,:);              %���г��Ϊ10ʱ��Ƶ�� ��ֵ ��԰ٷֱȡ�//��ӦK2�ĵ�10��ֵ����ֵΪ���ֵ����Ҫ��� harmonic10

%%%%%%%%%%%%%%%%%%%%%%%�ߴ�����%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  
% //1��nΪ�����������Σ� 
% //2��Ĭ��positionΪ-5��-4��-3��-2 -1 0 1 2 3 4 5����Ҳ������Ϊ�����������Ϊ��Σ�
position=[-5 -4 -3 -2 -1 0 1 2 3 4 5];    %λ��
f1=fmax/2+n*position;
num_f1=floor(f1/df)+1;
num_zx=floor((fmax/2)/df)+1;
[fuzhi_biandai]=vi(num_f1);               %��ֵ
[valu_biandai]=f(num_f1);                 %Ƶ��
if n==0
    k=zeros(1,11);                      %�״�
else
    k=[valu_biandai]./n;                      %�״�
end
dB=20*log10([fuzhi_biandai]./vi(num_zx));  %dB
biandai=[position',valu_biandai',fuzhi_biandai',k',dB'] ;%�����λ�� Ƶ�� ��ֵ �״� dB�� 

biandai_1=biandai(1,:);                    %�����ͬλ�õġ�λ�� Ƶ�� ��ֵ �״� dB��//����Ӧposition�ĵ�1��ֵ����ֵΪ���ֵ����Ҫ��� sidcband1
biandai_2=biandai(2,:);                    %�����ͬλ�õġ�λ�� Ƶ�� ��ֵ �״� dB��//����Ӧposition�ĵ�2��ֵ����ֵΪ���ֵ����Ҫ��� sidcband2
biandai_3=biandai(3,:);                    %�����ͬλ�õġ�λ�� Ƶ�� ��ֵ �״� dB��//����Ӧposition�ĵ�3��ֵ����ֵΪ���ֵ����Ҫ��� sidcband3
biandai_4=biandai(4,:);                    %�����ͬλ�õġ�λ�� Ƶ�� ��ֵ �״� dB��//����Ӧposition�ĵ�4��ֵ����ֵΪ���ֵ����Ҫ��� sidcband4
biandai_5=biandai(5,:);                    %�����ͬλ�õġ�λ�� Ƶ�� ��ֵ �״� dB��//����Ӧposition�ĵ�5��ֵ����ֵΪ���ֵ����Ҫ��� sidcband5
biandai_6=biandai(6,:);                    %�����ͬλ�õġ�λ�� Ƶ�� ��ֵ �״� dB��//����Ӧposition�ĵ�6��ֵ����ֵΪ���ֵ����Ҫ��� sidcband6
biandai_7=biandai(7,:);                    %�����ͬλ�õġ�λ�� Ƶ�� ��ֵ �״� dB��//����Ӧposition�ĵ�7��ֵ����ֵΪ���ֵ����Ҫ��� sidcband7
biandai_8=biandai(8,:);                    %�����ͬλ�õġ�λ�� Ƶ�� ��ֵ �״� dB��//����Ӧposition�ĵ�8��ֵ����ֵΪ���ֵ����Ҫ��� sidcband8
biandai_9=biandai(9,:);                    %�����ͬλ�õġ�λ�� Ƶ�� ��ֵ �״� dB��//����Ӧposition�ĵ�9��ֵ����ֵΪ���ֵ����Ҫ��� sidcband9
biandai_10=biandai(10,:);                  %�����ͬλ�õġ�λ�� Ƶ�� ��ֵ �״� dB��//����Ӧposition�ĵ�10��ֵ����ֵΪ���ֵ����Ҫ��� sidcband10
biandai_11=biandai(11,:);                  %�����ͬλ�õġ�λ�� Ƶ�� ��ֵ �״� dB��//����Ӧposition�ĵ�11��ֵ����ֵΪ���ֵ����Ҫ��� sidcband11
%%%%%%%%%%%%%%%%%%%%%%%%������ͼ������%%%%%%%%%%%%%%%%%%%%%%%%
f_plot=f;   %���᣺Ƶ��
v_plot=vi; %���᣺��ֵ
[~,f_judge]=min(abs(f_plot-fmax));
i=f_judge+1:length(f_plot);
f_plot(i)=[];
v_plot(i)=[];
%%%%%%%%%%%%%%%%%%%%%%%%ͼ��ʾ��%%%%%%%%%%%%%%%%%%%%%%%%   //ͼ��ʾ�����ֲ��漰���ò���ΪMatLab���ͼ��ʹ�ã�
figure;
plot(f_plot,v_plot);
xlim([fmin,fmax]); 
ylim([0,1.25*p]); 
title(['ͨ��',num2str(c),'���ٶ�Ƶ��ͼ����������']);
xlabel('Ƶ��      Hz');
ylabel('��ֵ      mm/s');
hold on;
plot([0,mf],[p,p],'r','linewidth',3);
s1=sprintf('(%2.4f, %2.4f)',mf,p);
text(mf,p,['��ֵ�㣺',s1]);