function [R,I,Complex]=Once_integral(w,A)
nn=length(w);
R=imag(A)./w;
I=real(A)./w;
Complex=R+1i*I;
Complex(1)=0;
Complex(nn)=0;
end

