legends = {'Business', 'Security', 'Shopper'};

coeff = pca(Untitled, 3);
x = 1;
y = 2;

tit = 'From (200,302) to (330,322)';
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