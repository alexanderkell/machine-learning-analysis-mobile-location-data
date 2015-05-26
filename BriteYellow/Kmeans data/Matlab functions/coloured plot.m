%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Track Generation
%% Name: Fezan Bux
%% Date: 13%04%2015
%% Date of Update: 23%04%2015
%% Description: Coloured plot
%% Reference: Chapter 7.3 Report or User Guide
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%load the data sets
X = [phone_id_num pca1 pca2];
s=15;    %size for the points

%assign data points with relative phone ID
X_1 = X(X(:,1)==1,:);   %ZX1B23QFSP48abead89f52e3bb   = 1 business
X_2 = X(X(:,1)==2,:);   %HT25TW5055273593c875a9898b00 = 2 business
X_3 = X(X(:,1)==3,:);   %ZX1B23QBS53771758c578bbd85   = 3 security
X_4 = X(X(:,1)==4,:);   %TA92903URNf067ff16fcf8e045   = 4 shopper
X_5 = X(X(:,1)==5,:);   %YT910K6675876ded0861342065   = 5 shopper

%plot the data points in turns
scatter(X_1(:,2),X_1(:,3),s,'k','filled')
hold on
scatter(X_2(:,2),X_2(:,3),s,'k','filled')
scatter(X_3(:,2),X_3(:,3),s,'b','filled')
scatter(X_4(:,2),X_4(:,3),s,'r','filled')
scatter(X_5(:,2),X_5(:,3),s,'r','filled')
hold off
xlabel('PCA column 1'), ylabel('PCA column 2')
grid on