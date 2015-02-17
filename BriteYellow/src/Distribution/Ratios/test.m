
[FileName,PathName] = uigetfile('*.csv','Select a file');

FILENAME = strcat(PathName, FileName);


fid = fopen(FILENAME);

tit = fgetl(fid);
xaxis = fgetl(fid);
yaxis = fgetl(fid);

legends = {'Business', 'Security', 'Shopper'};

i = 4;
Array=csvread(FILENAME, i,0);

business_X = [];
business_Y = [];

security_X = [];
security_Y = [];

shopper_X = [];
shopper_Y = [];


for i = i:length(Array)
    
    phone = Array(i, 3);
    if phone == 1 || phone == 5
        business_X(length(business_X)+1) = Array(i, 1);
        business_Y(length(business_Y)+1) = Array(i, 2);
    
    elseif phone == 2
        security_X(length(security_X)+1) = Array(i, 1);
        security_Y(length(security_Y)+1) = Array(i, 2);
    elseif phone == 3 || phone == 4
        shopper_X(length(shopper_X)+1) = Array(i, 1);
        shopper_Y(length(shopper_Y)+1) = Array(i, 2);
    end 
end

figure;
hold on;

scatter(business_X, business_Y)
scatter(security_X, security_Y)
scatter(shopper_X, shopper_Y)

xlabel(xaxis)
ylabel(yaxis)
title(tit)
legend(legends)
hold off;

fclose('all');