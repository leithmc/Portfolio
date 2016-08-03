function [c] = dct2(N, x)

% N = width and height of the block to perform the transform on
%N=input('Enter the value of N');
% x = 2-d array of values in the block -- array must have exactly N columns
disp('the input sequence is');
disp(x);
lx=length(x);
X=[x zeros(1,N-lx)];   % Parse the contents of x as a numeric matrix
X = double(X);         % Convert to matrix of double
CN=zeros(N);           % Create an NxN matrix to hold the cosine values
for n=0:N-1            % loop through rows
    for k=0:N-1        % loop through columns
        if k==0
            CN(k+1,n+1)=sqrt(1/N);  % set the first cell in the row to sqrt(1/N)
        else         
            CN(k+1,n+1)=sqrt(2/N)*cos(pi*(n+0.5)*k/N);  % set the rest to dct formula
            % the cos() part of the function applies the base DCT to the value
            % the sqrt(k/N) part of the function makes the DCT orthogonal and 
            %   therefore reversible
        end;
    end;
end;
disp('The Cn matrix is');
disp(CN);
c=CN * X.'; % get the real number DCT output by multiplying the matrix of cosine 
            %   values by the input matrix
disp('Output dct real sequence is-');
disp(c');
subplot(211);
stem(X);
title('Input sequence');
subplot(212);
stem(c);
title('Output dct sequence');
c = c';
end;