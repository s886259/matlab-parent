%%%%%%%%%%%%%%%%%%%%%%%%����Ƶ��ͼ����У�%%%%%%%%%%%%%%%%%%%%%%%%
clear;
clc;
c=8;    %������Ҫ������ͨ�����
aa=xlsread('1417.xlsx',2);
a=aa(:,c);
g=9.8;
%%%%%%%%%%%%%%%%%%%%%%%%��ĸ˵��%%%%%%%%%%%%%%%%%%%%%%%%
    %   ��λ��gE
    %   fcut����Ƶ��ֹ
    %   fmin����ʼƵ�ʣ�fmax������Ƶ�ʣ�fmin~fmaxΪƵ�ʷ�Χ
    %   fbegin~fstop����������Χ��500~10k
    %   ymax�����̶�
    %   ���ֵ����ֵ
    %   p����ֵ��mf����ֵ��Ӧ��Ƶ��
    %   TV������ֵ=����Ƶ��=�������ƣ�m/s^2
    %   �����Ϣ������������Ϣ���ע��
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%    
fs=25600;   %����Ƶ��
N=length(a); %���ݳ���
df=fs/N;    %Ƶ�ʷֱ���
fmin=2;fmax=1000;  %fmin����ʼƵ�ʣ�famx����ֹƵ��
flcut=500;          %��Ƶ��ֹ
fhcut=10000;         %��Ƶ��ֹ
[a_fir]=hann_filt(a,fs,flcut,fhcut);
a_fir_2=a_fir.^2;
[a_fir_3]=hann_filt(a_fir_2,fs,flcut,fhcut);

[f,ai]=spectrum(fs,a_fir_3);    %ai���ڴ洢Ƶ�׷�ֵ����
[p,m]=max(ai(2:500));  %Ѱ��
mf=f(m);    %��ֵ��ӦƵ��ֵ
[TV]=total_value(a_fir,fs,fmin,fmax);  %����Ƶ�� (Ҳ�� �������ƣ�
%%%%%%%%%%%%%%%%%%%%%%%FAM������%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  
BPFI=9.429032;BPFO=6.570968;BSF=2.645376;FTF=0.410686;
n=12;                      %תƵ
k1=n*[1 2 3 4];      %г��(���������
f_BPFI=k1*BPFI;f_BPFO=k1*BPFO;f_BSF=k1*BSF;f_FTF=k1*FTF;
num_BPFI=floor(f_BPFI/df)+1;
num_BPFO=floor(f_BPFO/df)+1;
num_BSF=floor(f_BSF/df)+1;
num_FTF=floor(f_FTF/df)+1;
r_BPFI=f(num_BPFI);      %BPFIʵ��Ƶ��
r_BPFO=f(num_BPFO);      %PBF0ʵ��Ƶ��
r_BSF=f(num_BSF);       %BSFʵ��Ƶ��
r_FTF=f(num_FTF);       %FIFʵ��Ƶ��
valu_BPFI=ai(num_BPFI);  %BPFI��ֵ
valu_BPFO=ai(num_BPFO);  %BPF0��ֵ
valu_BSF=ai(num_BSF);    %BSF��ֵ
valu_FTF=ai(num_FTF);    %FIF��ֵ
output_BPFI=[r_BPFI',valu_BPFI];   
output_BPFO=[r_BPFO',valu_BPFO];   
output_BSF=[r_BSF',valu_BSF];      
output_FTF=[r_FTF',valu_FTF];     
BPFI_1=output_BPFI(1,:);          %���BPFI*1��Ƶ�ʺͷ�ֵ
BPFI_2=output_BPFI(2,:);          %���BPFI*2��Ƶ�ʺͷ�ֵ
BPFI_3=output_BPFI(3,:);          %���BPFI*3��Ƶ�ʺͷ�ֵ
BPFI_4=output_BPFI(4,:);          %���BPFI*4��Ƶ�ʺͷ�ֵ

BPFO_1=output_BPFO(1,:);          %���BPFO*1��Ƶ�ʺͷ�ֵ
BPFO_2=output_BPFO(2,:);          %���BPFO*2��Ƶ�ʺͷ�ֵ
BPFO_3=output_BPFO(3,:);          %���BPFO*3��Ƶ�ʺͷ�ֵ
BPFO_4=output_BPFO(4,:);          %���BPFO*4��Ƶ�ʺͷ�ֵ

BSF_1=output_BSF(1,:);          %���BSF*1��Ƶ�ʺͷ�ֵ
BSF_2=output_BSF(2,:);          %���BSF*2��Ƶ�ʺͷ�ֵ
BSF_3=output_BSF(3,:);          %���BSF*3��Ƶ�ʺͷ�ֵ
BSF_4=output_BSF(4,:);          %���BSF*4��Ƶ�ʺͷ�ֵ

FTF_1=output_FTF(1,:);          %���FTF*1��Ƶ�ʺͷ�ֵ
FTF_2=output_FTF(2,:);          %���FTF*2��Ƶ�ʺͷ�ֵ
FTF_3=output_FTF(3,:);          %���FTF*3��Ƶ�ʺͷ�ֵ
FTF_4=output_FTF(4,:);          %���FTF*4��Ƶ�ʺͷ�ֵ
%%%%%%%%%%%%%%%%%%%%%%%г��������%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  
% n=20;                       %תƵ
k2=[1 2 3 4 5 6 7 8 9 10];   %г�������������
f_xiebo=k2*n;   %г��
num_f=floor(f_xiebo/df)+1;
[valu_xiebo]=f(num_f);        %г��Ƶ��
[fuzhi_xiebo]=ai(num_f);        %г����ֵ
percent=100*(fuzhi_xiebo./fuzhi_xiebo(1)); %����ڻ�Ƶ�İٷֱ�
xiebo=[valu_xiebo',fuzhi_xiebo,percent];  
xiebo_1=xiebo(1,:);                %���г��Ϊ1ʱ��Ƶ�� ��ֵ ��԰ٷֱȡ�
xiebo_2=xiebo(2,:);                %���г��Ϊ2ʱ��Ƶ�� ��ֵ ��԰ٷֱȡ�
xiebo_3=xiebo(3,:);                %���г��Ϊ3ʱ��Ƶ�� ��ֵ ��԰ٷֱȡ�
xiebo_4=xiebo(4,:);                %���г��Ϊ4ʱ��Ƶ�� ��ֵ ��԰ٷֱȡ�
xiebo_5=xiebo(5,:);                %���г��Ϊ5ʱ��Ƶ�� ��ֵ ��԰ٷֱȡ�
xiebo_6=xiebo(6,:);                %���г��Ϊ6ʱ��Ƶ�� ��ֵ ��԰ٷֱȡ�
xiebo_7=xiebo(7,:);                %���г��Ϊ7ʱ��Ƶ�� ��ֵ ��԰ٷֱȡ�
xiebo_8=xiebo(8,:);                %���г��Ϊ8ʱ��Ƶ�� ��ֵ ��԰ٷֱȡ�
xiebo_9=xiebo(9,:);                %���г��Ϊ9ʱ��Ƶ�� ��ֵ ��԰ٷֱȡ�
xiebo_10=xiebo(10,:);              %���г��Ϊ10ʱ��Ƶ�� ��ֵ ��԰ٷֱȡ�
%%%%%%%%%%%%%%%%%%%%%%%�ߴ�����%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  
n=0
if n~=0
num_n=floor(n/df)+1;      
fuzhi_zhuanpin=ai(num_n);   %תƵ��Ӧ�ķ�ֵ
position=[-5 -4 -3 -2 -1 0 1 2 3 4 5];  %λ��(���������
f1=fmax/2+n*position;
num_f1=floor(f1/df)+1;
num_zx=floor((fmax/2)/df)+1;
[fuzhi_biandai]=ai(num_f1);               %��ֵ
[valu_biandai]=f(num_f1);                 %Ƶ��
k=[valu_biandai]./n;                
dB=20*log10([fuzhi_biandai]./ai(num_zx));  %dB
biandai=[position',valu_biandai',fuzhi_biandai,k',dB] ;
biandai_1=biandai(1,:);                    %�����ͬλ�õġ�λ�� Ƶ�� ��ֵ �״� dB��
biandai_2=biandai(2,:);                    %�����ͬλ�õġ�λ�� Ƶ�� ��ֵ �״� dB��
biandai_3=biandai(3,:);                    %�����ͬλ�õġ�λ�� Ƶ�� ��ֵ �״� dB��
biandai_4=biandai(4,:);                    %�����ͬλ�õġ�λ�� Ƶ�� ��ֵ �״� dB��
biandai_5=biandai(5,:);                    %�����ͬλ�õġ�λ�� Ƶ�� ��ֵ �״� dB��
biandai_6=biandai(6,:);                    %�����ͬλ�õġ�λ�� Ƶ�� ��ֵ �״� dB��
biandai_7=biandai(7,:);                    %�����ͬλ�õġ�λ�� Ƶ�� ��ֵ �״� dB��
biandai_8=biandai(8,:);                    %�����ͬλ�õġ�λ�� Ƶ�� ��ֵ �״� dB��
biandai_9=biandai(9,:);                    %�����ͬλ�õġ�λ�� Ƶ�� ��ֵ �״� dB��
biandai_10=biandai(10,:);                  %�����ͬλ�õġ�λ�� Ƶ�� ��ֵ �״� dB��
biandai_11=biandai(11,:);                  %�����ͬλ�õġ�λ�� Ƶ�� ��ֵ �״� dB��
else
   k=0; 
   fuzhi_zhuanpin=0;   %תƵ��Ӧ�ķ�ֵ
end
%%%%%%%%%%%%%%%%%%%%%%%%ͼ��ʾ��%%%%%%%%%%%%%%%%%%%%%%%%
figure;
plot(f,ai);
xlim([fmin,fmax]); 
ylim([0,0.25]); 
title(['ͨ��',num2str(c),'�İ���Ƶ��ͼ']);
xlabel('Ƶ��      Hz');
ylabel('��ֵ      gE');
hold on;
%plot([0,mf],[p,p],'r','linewidth',3);
%s1=sprintf('(%2.5f, %2.5f)',mf,p);
%text('rotation',90 );
%text(mf,p,['��ֵ�㣺',s1]);
%text(mf,p,['��ֵ�㣺',s1],'rotation',90);
%��ע����
for i=1:4
    p1=num_FTF(i);
    p2=valu_FTF(i);
    p3=num_BSF(i);
    p4=valu_BSF(i);
    p5=num_BPFO(i);
    p6=valu_BPFO(i);
    p7=num_BPFI(i);
    p8=valu_BPFI(i);
    text(p1,0.2,['FTF',num2str(i)],'rotation',90);
    text(p3,0.2,['BSF',num2str(i)],'rotation',90);
    text(p5,0.2,['BPFO',num2str(i)],'rotation',90);
    text(p7,0.2,['BPFI',num2str(i)],'rotation',90);
    plot([p1,p1],[0,0.2],'-- r');
    plot([p3,p3],[0,0.2],'-- r');
    plot([p5,p5],[0,0.2],'-- r');
    plot([p7,p7],[0,0.2],'-- r');
end



