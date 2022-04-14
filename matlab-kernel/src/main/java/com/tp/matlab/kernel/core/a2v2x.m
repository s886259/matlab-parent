function [v,x]=a2v2x(a,fs,flcut,fhcut)
    a1=a;
    n=length(a1); %采样点数
    % t=(0:n-1)/fs; %时间
    a_fft=fft(detrend(a1),n);
    df=fs/n; 
    f1=-(fs/2):df:-df; 
    f2=0:df:(fs/2)-df;
    f=[f2,f1]';
    w=2*pi*f;
    n_inferior=round(flcut/df)+1; 
    n_superior=round(fhcut/df)+1; 
    [Rv,Iv,Complexv]=Once_integral(w,a_fft);
    k=zeros(1,n); 
    k(n_inferior:n_superior)=Complexv(n_inferior:n_superior); 
    k(n-n_superior+1:n-n_inferior+1)=Complexv(n-n_superior+1:n-n_inferior+1); 
    v_time=ifft(k);
    v=real(v_time(1:n)); 
    v=v';

    v_fft=fft(detrend(v),n);
    [Rx,Ix,Complexx]=Once_integral(w,v_fft);
    k=zeros(1,n);
    k(n_inferior:n_superior)=Complexx(n_inferior:n_superior); 
    k(n-n_superior+1:n-n_inferior+1)=Complexx(n-n_superior+1:n-n_inferior+1); 
    x_time=ifft(k);
    x=real(x_time(1:n));

end
