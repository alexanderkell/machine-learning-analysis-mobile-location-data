function soln = makeMovie( M )
%UNTITLED16 Summary of this function goes here
%   Detailed explanation goes here
img = imread('map.jpg');

%[new, coeff, main_components]=returnFiltered(M);

frames=size(M,1);

F2(frames) = struct('cdata',[],'colormap',[]);

% custom lines


figure
for i=1:size(M,1)

    imagesc([42 1100],[2 500],img);
    hold on
    if i > 1
        delete(a)
    end
    a = plot(M(1:i,1),M(1:i,2),'o','MarkerFaceColor','r','MarkerSize',5);
    
    hold off
    grid on
    colormap(gray);
   
    F2(i) = getframe;
end
soln=F2


end

