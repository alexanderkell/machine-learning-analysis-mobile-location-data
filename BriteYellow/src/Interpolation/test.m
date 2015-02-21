%pointx = ph1(1:20, 1);
%pointy = ph1(1:20, 2);

pointx = ph1(1:10, 1);
pointy = ph1(1:10, 2);
[P,R,S] = lagrangepoly(pointx,pointy);

yvalue = [];
xx = 150:300;

for i = 1:length(xx)
%plot(xx,polyval(P,xx),pointx,pointy,'or',R,S,'.b',   xx,spline(pointx,pointy,xx),'--g')
    yvalue(i) = getY(transpose(P),xx(i))
    
    
end
plot(pointx,pointy,'o',   xx,yvalue,'--g')
grid