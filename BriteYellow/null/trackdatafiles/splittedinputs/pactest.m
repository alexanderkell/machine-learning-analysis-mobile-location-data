legends = {'Business', 'Security', 'Shopper'};

coeff = pca(Untitled, 2);
x = 3;
y = 5;

tit = 'From (590,344) to (720,364)';
business = [];
security = [];
shopper = [];

for i = 1:length(People)

    if strcmp(People(i,1), 'Business')
        business(length(business)+1, :) = coeff(i,:);
    elseif strcmp(People(i),'Security')
        security(length(security)+1, :) = coeff(i, :);
    elseif strcmp(People(i),'Shopper')
        shopper(length(shopper)+1, :) = coeff(i, :);
    end
end

figure
hold on
scatter(business(:,x), business(:, y));
scatter(security(:,x), security(:,y));
scatter(shopper(:,x), shopper(:,y));
legend(legends);
title(tit);
hold off