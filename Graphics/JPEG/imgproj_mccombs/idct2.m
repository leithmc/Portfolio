function  [X] = idct2(N, c)
%close all;
%clear all;
%clc;
% N = width and height of the block to perform the transform on
%N=input('Enter the value of N');
%disp('N=');
%disp(N);
% c = 2-d array of dct result values in the block -- array must have exactly N columns
%c=input('Enter the input dct sequence');
disp('the input dct sequence is');
disp(c);
lc=length(c);
cc=[c zeros(1,N-lc)];  % Parse the contents of c as a numeric matrix
cc = double(cc);       % Convert to a matrix of doubles
CN=zeros(N);           % Create an NxN matrix to hold the cosine values
for n=0:N-1            % Loop through rows
    for k=0:N-1        % Loop through columns
        if k==0
            CN(k+1,n+1)=sqrt(1/N);   % set the first cell in the row to sqrt(1/N)
        else
            CN(k+1,n+1)=sqrt(2/N)*cos(pi*(n+0.5)*k/N);   % set the rest to idct formula
            % the cos() part of the function applies the base IDCT to the value
            % the sqrt(k/N) part of the function makes the IDCT orthogonal and therefore reversible
        end;
    end;
end;
X=CN.'*cc.';  % get the real number IDCT output by multiplying the matrix of cosine values by the input matrix
disp('o/p inverse dct real sequence is-');
disp(X.');
subplot(211);
stem(c);
title('Input dct sequence');
subplot(212);
stem(X);
title('Output inverse dct sequence');
X = X';
end;