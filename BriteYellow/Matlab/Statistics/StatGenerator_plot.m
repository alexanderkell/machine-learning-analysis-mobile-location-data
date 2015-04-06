data = From200330to302322;

index1 = 2;
index2 = 5;

business_X = [];
business_Y = [];

security_X = [];
security_Y = [];

shopper_X = [];
shopper_Y = [];

     
for i = 1:length(PhoneID)
    
    phone = PhoneID(i);
    
    codition2 = (strcmp(phone(1), 'ZX1B23QFSP48abead89f52e3bb'))
    if (strcmp(phone(1), 'HT25TW5055273593c875a9898b00')) || (strcmp(phone(1), 'ZX1B23QFSP48abead89f52e3bb'))
        business_X(length(business_X)+1) = data(i, index1);
        business_Y(length(business_Y)+1) = data(i, index2);
    
    elseif (strcmp(phone(1), 'ZX1B23QBS53771758c578bbd85'));
        security_X(length(security_X)+1) = data(i, index1);
        security_Y(length(security_Y)+1) = data(i, index2);
    elseif (strcmp(phone(1), 'TA92903URNf067ff16fcf8e045')) || (strcmp(phone(1), 'YT910K6675876ded0861342065'))
        shopper_X(length(shopper_X)+1) = data(i, index1);
        shopper_Y(length(shopper_Y)+1) = data(i, index2);
    end 
end

figure;
hold on;

scatter(business_X, business_Y)
scatter(security_X, security_Y)
scatter(shopper_X, shopper_Y)

%xlabel(xaxis)
%ylabel(yaxis)
%title(tit)
legend(legends)
hold off;