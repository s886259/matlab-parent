function [TV]=total_value(v,fs,fmin,fmax) %aΪ�˲�֮���ʱ������
n=length(v);
df=fs/n;
n_inferior=round(fmin/df)+1; 
n_superior=round(fmax/df)+1; 
A=abs(fft(v,n)*2/n);    %ÿ����ֵ
%plot(f,A(1:n/2));
a1=0;
for i=n_inferior:n_superior
    a1=a1+A(i)^2;   
end
for i=n-n_superior:n-n_inferior
    a1=a1+A(i)^2;   
end
TV=sqrt(a1)/sqrt(1.5);
end
    
