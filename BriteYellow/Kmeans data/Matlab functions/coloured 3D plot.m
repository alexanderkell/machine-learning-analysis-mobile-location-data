%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Track Generation
%% Name: Fezan Bux
%% Date: 13%04%2015
%% Date of Update: 23%04%2015
%% Description: Coloured 3D plot
%% Reference: Chapter 7.3 Report or User Guide
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%load the data sets
X = [phone_id_num data1 data2 data3];
s=15;   %size for the points

%assign data points with relative phone ID
X_1 = X(X(:,1)==1,:);   %ZX1B23QFSP48abead89f52e3bb   = 1 business
X_2 = X(X(:,1)==2,:);   %HT25TW5055273593c875a9898b00 = 2 business
X_3 = X(X(:,1)==3,:);   %ZX1B23QBS53771758c578bbd85   = 3 security

%plot the data points in turns
scatter3(X_1(:,2),X_1(:,3),X_1(:,4),s,'k','filled')
hold on
scatter3(X_2(:,2),X_2(:,3),X_2(:,4),s,'k','filled')
scatter3(X_3(:,2),X_3(:,3),X_3(:,4),s,'b','filled')
scatter3(X_4(:,2),X_4(:,3),X_4(:,4),s,'r','filled')
scatter3(X_5(:,2),X_5(:,3),X_5(:,4),s,'r','filled')
hold off
xlabel('totAvrgSpeed'), ylabel('sThetaChange'), zlabel('timePerShortest')
grid on