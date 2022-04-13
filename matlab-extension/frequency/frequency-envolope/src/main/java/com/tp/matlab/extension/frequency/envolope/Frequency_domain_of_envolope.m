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
    %   TV������ֵ=����Ƶ��=�������ƣ�m/s^2    //��ֵΪ���ֵ����Ҫ���
    %   �����Ϣ������������Ϣ���ע��
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%    
% //1������Ƶ�ʺ����ݳ���Ϊ��Σ�
% //2���˲�����
% //1��ÿ��ͼ���й̶����˲������ã�
% //2��Ҳ������Ϊ�������ʵ��ͼ�����¼��㣺fmin��fmax��flcut��fhcut���ֶΣ�
fs=25600;          %����Ƶ��
N=length(a);       %���ݳ���
fmin=0;            %fmin����ʼƵ��
fmax=1000;         %famx����ֹƵ��
flcut=500;         %��Ƶ��ֹ
fhcut=10000;       %��Ƶ��ֹ
df=fs/N;
[a_fir]=hann_filt(a,fs,flcut,fhcut);
a_fir_2=a_fir.^2;
[a_fir_3]=hann_filt(a_fir_2,fs,flcut,fhcut);
[f,ai]=spectrum(fs,a_fir_3);    %ai���ڴ洢Ƶ�׷�ֵ����
[p,m]=max(ai(1:fmax));  %Ѱ��
mf=f(m);    %��ֵ��ӦƵ��ֵ
[TV]=total_value(a_fir,fs,fmin,fmax);  %����Ƶ�� (Ҳ�� �������ƣ�
%%%%%%%%%%%%%%%%%%%%%%%FAM������%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  
% //1��BPFI��BPFO��BSF��FTF��nΪ�����������Σ�
% //2��Ĭ��K1�ı�ƵΪ1��2��3��4ȥ���Լ��㣬��Ҳ������Ϊ�����������Ϊ��Σ�
BPFI=9.429032;BPFO=6.570968;BSF=2.645376;FTF=0.410686;
n=12;                      %תƵ(���������
k1=n*[1 2 3 4];            %��Ƶ
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
valu_FTF=ai(num_FTF);    %BFIF��ֵ
output_BPFI=[r_BPFI',valu_BPFI];   %���BPFI��Ƶ�ʺͷ�ֵ   
output_BPFO=[r_BPFO',valu_BPFO];   %���BPFO��Ƶ�ʺͷ�ֵ  
output_BSF=[r_BSF',valu_BSF];      %���BSF��Ƶ�ʺͷ�ֵ    
output_FTF=[r_FTF',valu_FTF];      %���FTF��Ƶ�ʺͷ�ֵ    

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
n=12;                           %תƵ
num_n=floor(n/df)+1;
k2=[1 2 3 4 5 6 7 8 9 10];      %г��
f_xiebo=k2*n;   %г��
num_f=floor(f_xiebo/df)+1;
[valu_xiebo]=f(num_f);          %г��Ƶ��
[fuzhi_xiebo]=ai(num_f);        %г����ֵ
percent=100*(fuzhi_xiebo./fuzhi_xiebo(1)); %����ڻ�Ƶ�İٷֱ�
xiebo=[valu_xiebo',fuzhi_xiebo,percent];  %�����Ƶ�� ��ֵ ��԰ٷֱȡ� //��ֵΪ���ֵ����Ҫ���

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
position=[-5 -4 -3 -2 -1 0 1 2 3 4 5];     %λ��
f1=fmax/2+n*position;
num_f1=floor(f1/df)+1;
num_zx=floor((fmax/2)/df)+1;
[fuzhi_biandai]=ai(num_f1);                %��ֵ
[valu_biandai]=f(num_f1);                  %Ƶ��
k=[valu_biandai]./(n+eps);                       %�״�
dB=20*log10([fuzhi_biandai]./ai(num_zx));  %dB
biandai=[position',valu_biandai',fuzhi_biandai,k',dB] ;%�����λ�� Ƶ�� ��ֵ �״� dB��

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
Am_plot=ai; %���᣺��ֵ
%%%%%%%%%%%%%%%%%%%%%%%%ͼ��ʾ��%%%%%%%%%%%%%%%%%%%%%%%%   //ͼ��ʾ�����ֲ��漰���ò���ΪMatLab���ͼ��ʹ�ã�
figure;
plot(f_plot,Am_plot);
xlim([fmin,fmax]); 
ylim([0,1.5*p]); 
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
    text(p1,1.2*p,['FTF',num2str(i)],'rotation',90);
    text(p3,1.2*p,['BSF',num2str(i)],'rotation',90);
    text(p5,1.2*p,['BPFO',num2str(i)],'rotation',90);
    text(p7,1.2*p,['BPFI',num2str(i)],'rotation',90);
    plot([p1,p1],[0,1.2*p],'-- r');
    plot([p3,p3],[0,1.2*p],'-- r');
    plot([p5,p5],[0,1.2*p],'-- r');
    plot([p7,p7],[0,1.2*p],'-- r');
end



