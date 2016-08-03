%pic = imread ('tree_frog.jpg');
%figure(1); imshow(pic);
%[rows cols colors] = size(pic)

%for row = [1:rows],
%  for col = [1:cols],
%    temp = pic(row,col,1);
%    pic(row,col,1) = pic(row,col,2);
%    pic(row,col,2) = temp;
%  end;
%end;
%figure(2); imshow(pic);
    
pic = imread ('nautilus.jpg');
figure(1); imshow(pic);
[rows cols] = size(pic)
for row = [1:rows],
  for col = [1:cols],
    if (pic(row, col) < 85) pic(row, col) = 1;
    elseif (pic(row, col) > 170) pic(row, col) = 255;
    else pic(row, col) = 128;
    endif;
  end;
end;
figure(2); imshow(pic);
  
pkg load image
pic = imread ('nautilus.jpg');
img1 = edge (pic, 'Sobel');
img2 = edge (pic, 'Canny');
img3 = edge (pic, 'log');
figure(3); imshow(img1);
figure(4); imshow(img2);
figure(5); imshow(img3);
